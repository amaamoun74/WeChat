package com.example.wechat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wechat.R;
import com.example.wechat.pojo.Bottom_Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mailET, passwordET;
    TextView signup , forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        Button login = findViewById(R.id.logInBtn);
        login.setOnClickListener(view -> validate());

        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(view -> firebaseForgetPassword());

        signup = findViewById(R.id.signUp_Text);
        signup.setOnClickListener(view -> startActivity(new Intent(Login.this, SignUp.class)));
    }

    void validate() {
     if (mailET.getText().toString().isEmpty()) {
            mailET.setError("fill in mail field");
            mailET.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(mailET.getText().toString()).matches()) {
            mailET.setError("should be in ****@example.com format");
            mailET.requestFocus();

        } else if (passwordET.getText().toString().isEmpty()) {
            passwordET.setError("fill in Password field");
            passwordET.requestFocus();

        } else if (passwordET.getText().toString().length() < 6) {
            passwordET.setError("Min Password length Is 6 char ");
            passwordET.requestFocus();
        } else {
         String password = passwordET.getText().toString();
         String email = mailET.getText().toString();
            firebaseLogin(email, password);
        }
    }

    private void firebaseLogin( String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if(task.isSuccessful()) {
                                                   startActivity(new Intent(Login.this, Bottom_Navigation.class));
                                               }
                                               else{
                                                   Toast.makeText(Login.this, "failed to login: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }
                );
    }


    void firebaseForgetPassword() {
        if (!Patterns.EMAIL_ADDRESS.matcher(mailET.getText().toString()).matches()) {
            mAuth.sendPasswordResetEmail(mailET.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "check your E-mail to reset your password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}