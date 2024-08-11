package foody.vn.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import foody.vn.R;

public class PasswordRecoveryActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRecovery;
    TextView txtNewSignUp;
    EditText edEmailRecovery;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgotten_password);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRecovery = (Button) findViewById(R.id.btnRecovery);
        txtNewSignUp = (TextView) findViewById(R.id.txtNewSignUp);
        edEmailRecovery = (EditText) findViewById(R.id.edEmailRecovery);

        btnRecovery.setOnClickListener(this);
        txtNewSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnRecovery){
            String email = edEmailRecovery.getText().toString();
            boolean checkMail = CheckEmail(email);

            if(checkMail){
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PasswordRecoveryActivity.this, getString(R.string.email_sending_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(PasswordRecoveryActivity.this, getString(R.string.invalid_email_message), Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.txtNewSignUp) {
            Intent iNewSignUp = new Intent(PasswordRecoveryActivity.this, SignUpActivity.class);
            startActivity(iNewSignUp);
        }
    }
    private boolean CheckEmail (String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
