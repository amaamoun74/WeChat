package com.example.wechat.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.wechat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText nameET, mailET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.name);
        mailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        Button signUp = findViewById(R.id.signupBtn);
        signUp.setOnClickListener(view -> validate());
    }
    void validate() {
        if (nameET.getText().toString().isEmpty()) {
            nameET.setError("fill in name field");
            nameET.requestFocus();

        } else if (mailET.getText().toString().isEmpty()) {
            mailET.setError("fill in mail field");
            mailET.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(mailET.getText().toString()).matches()) {
            mailET.setError("should be in ****@example.com format");
            mailET.requestFocus();

        } else if (passwordET.getText().toString().isEmpty()) {
            passwordET.setError("fill in Password field");
            passwordET.requestFocus();

        }
        else if (passwordET.getText().toString().length() < 6) {
            passwordET.setError("Min Password length Is 6 char ");
            passwordET.requestFocus();
        }

        else {
            String name = nameET.getText().toString();
            String password = passwordET.getText().toString();
            String email = mailET.getText().toString();
            firebaseCreateUser(name, email, password);
        }
    }

    private void firebaseCreateUser(String name , String email , String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setUserData(name);
                    } else {
                        Toast.makeText(getApplicationContext(),"Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setUserData(String name){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String userID = firebaseUser.getUid();
        // reference = FirebaseDatabase.getInstance().getReference("Users")
        //       .child(userID);

        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("id",userID);
        hashMap.put("username",name);
        hashMap.put("imageURL","def");
        hashMap.put("status","offline");
        hashMap.put("search",name.toLowerCase());


        FirebaseDatabase.getInstance().getReference("Users")
                .child(userID)
                .setValue(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                        assert firebaseUser1 != null;
                        firebaseUser1.sendEmailVerification();
                           Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(SignUp.this, MainActivity.class));
                                       } else {
                                            Toast.makeText(SignUp.this, "Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                    });
    }

}