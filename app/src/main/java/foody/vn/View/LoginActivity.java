package foody.vn.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

import foody.vn.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {
    Button btnLoginWithGoogle;
    Button btnLoginWithFacebook;
    Button btnLogin;
    EditText edEmailLogin, edPasswordLogin;
    TextView txtSignUp;
    TextView txtForgottenPwd;
    GoogleApiClient apiClient;
    public static int REQUESTCODE_LOGIN_GOOGLE = 99;
    public static int CHECK_PROVIDER_LOGIN = 0;
    FirebaseAuth firebaseAuth;
    CallbackManager mCallbackFacebook;
    LoginManager loginManager;
    List<String> permissionFacebook = Arrays.asList("email", "public_profile");
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_login);

        mCallbackFacebook = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        btnLoginWithGoogle = (Button) findViewById(R.id.btnLogInWithGoogle);
        btnLoginWithFacebook = (Button) findViewById(R.id.btnLogInWithFacebook);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edEmailLogin = (EditText) findViewById(R.id.edEmailLogin);
        edPasswordLogin = (EditText) findViewById(R.id.edPasswordLogin);
        txtSignUp = (TextView)  findViewById(R.id.txtSignUp);
        txtForgottenPwd = (TextView) findViewById(R.id.txtForgottenPwd);


        btnLoginWithGoogle.setOnClickListener(this);
        btnLoginWithFacebook.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtForgottenPwd.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("saveLogin", MODE_PRIVATE);

        CreateClientLoginGoogle();
    }

    private void LoginFacebook(){
        loginManager.logInWithReadPermissions(this, permissionFacebook);
        loginManager.registerCallback(mCallbackFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                CHECK_PROVIDER_LOGIN = 2;
                String tokenID = loginResult.getAccessToken().getToken();
                AuthenticationLoginFirebase(tokenID);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });
    }

    //Init create client for login Google
    private void CreateClientLoginGoogle() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken( getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

    }
    //Open form login Google
    private void LoginGoogle (GoogleApiClient apiClient){
        CHECK_PROVIDER_LOGIN = 1;
        Intent iLoginGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iLoginGoogle, REQUESTCODE_LOGIN_GOOGLE);
    }
    private void Login (){
        String email = edEmailLogin.getText().toString();
        String password = edPasswordLogin.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, getString(R.string.failed_login_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }


    //Get tokenID logged in Google to login Firebase
    private void AuthenticationLoginFirebase(String tokenID){
        if(CHECK_PROVIDER_LOGIN == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID, null);
            firebaseAuth.signInWithCredential(authCredential);
        } else if (CHECK_PROVIDER_LOGIN == 2) {
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
            firebaseAuth.signInWithCredential(authCredential);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE_LOGIN_GOOGLE){
            if(resultCode == RESULT_OK){
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = null;
                if (signInResult != null) {
                    account = signInResult.getSignInAccount();
                }
                String tokenID = null;
                if (account != null) {
                    tokenID = account.getIdToken();
                }
                AuthenticationLoginFirebase(tokenID);
            }
        } else {
            mCallbackFacebook.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Listen user click in button login google, facebook and email account
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnLogInWithGoogle){
            LoginGoogle(apiClient);
        } else if (id == R.id.btnLogInWithFacebook) {
            LoginFacebook();
        } else if (id == R.id.txtSignUp) {
            Intent iSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(iSignUp);
        } else if (id ==R.id.btnLogin) {
            Login();
        } else if (id == R.id.txtForgottenPwd){
            Intent iPasswordRecovery = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(iPasswordRecovery);
        }
    }

    //Check user logged in success or log out
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userID", user.getUid());
            editor.apply();

            Intent iHomePage = new Intent(this, HomePageActivity.class);
            startActivity(iHomePage);
        }
    }
}
