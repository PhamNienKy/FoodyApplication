package foody.vn.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import foody.vn.Controller.SignUpController;
import foody.vn.Model.UserModel;
import foody.vn.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSignUp;
    EditText edEmailSignUp, edPasswordSignUp, edRepeatPwd;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    SignUpController signUpController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        edEmailSignUp = (EditText) findViewById(R.id.edEmailSignUp);
        edPasswordSignUp = (EditText) findViewById(R.id.edPasswordSignUp);
        edRepeatPwd = (EditText) findViewById(R.id.edRepeatPwd);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setIndeterminate(true);

        progressDialog.show();

        final String email = edEmailSignUp.getText().toString();
        String password = edPasswordSignUp.getText().toString();
        String repeatPwd = edRepeatPwd.getText().toString();
        String errorMessage = getString(R.string.error_signup_message);

        if(email.trim().isEmpty()){
            errorMessage += " " + getString(R.string.email);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else if (password.trim().isEmpty()) {
            errorMessage += " " + getString(R.string.password);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else if (!repeatPwd.equals(password)) {
            Toast.makeText(this, getString(R.string.repeat_pwd_message), Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        UserModel userModel = new UserModel();
                        userModel.setUser_name(email);
                        userModel.setUser_image("user.png");
                        String uid = task.getResult().getUser().getUid();

                        signUpController = new SignUpController();
                        signUpController.AddInfoUserController(userModel, uid);
                        Toast.makeText(SignUpActivity.this, getString(R.string.success_signup_message), Toast.LENGTH_SHORT).show();
                    }
            });
        }
    }
}
