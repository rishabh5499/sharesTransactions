package com.example.sharestransactions.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharestransactions.MainActivity;
import com.example.sharestransactions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity {

    EditText email, pass, confirm;
    Button signup;
    TextView signin;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextSignUp);
        pass = findViewById(R.id.passwordSignUp);
        confirm = findViewById(R.id.confirmSignUp);
        signup = findViewById(R.id.buttonSignUp);
        signin = findViewById(R.id.textViewSignUp4);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstr = email.getText().toString().trim();
                String passstr = pass.getText().toString().trim();
                String confirmstr = confirm.getText().toString().trim();

                if (emailstr.isEmpty()) {
                    email.setError("Enter Email ID");
                    email.requestFocus();
                } else if (passstr.isEmpty()) {
                    pass.setError("Enter Password");
                    pass.requestFocus();
                } else if (confirmstr.isEmpty()) {
                    confirm.setError("Confirm Password");
                    confirm.requestFocus();
                }
                else if (emailstr.isEmpty() && passstr.isEmpty() && confirmstr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter all the data", Toast.LENGTH_SHORT).show();
                } else if (!(emailstr.isEmpty() && passstr.isEmpty() && confirmstr.isEmpty())) {
                    if(passstr.equals(confirmstr)) {
                        mFirebaseAuth.createUserWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(signUp.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords dont match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signin = new Intent(signUp.this, login.class);
                startActivity(signin);
                finish();
            }
        });
    }
}