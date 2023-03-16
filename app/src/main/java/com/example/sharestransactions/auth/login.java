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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    EditText email, pass;
    TextView signup;
    Button login;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextLogin);
        pass = (EditText)findViewById(R.id.passwordLogin);
        signup = findViewById(R.id.textViewLogin3);
        login = findViewById(R.id.buttonLogin);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                String uid = FirebaseAuth.getInstance().getUid();
                if(mFirebaseUser!=null) {
                    Intent i = new Intent(login.this, MainActivity.class);
                    startActivity(i);
                } else {

                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstr = email.getText().toString().trim();
                String passstr =  pass.getText().toString().trim();

                if(emailstr.isEmpty()){
                    email.setError("Enter Email ID");
                    email.requestFocus();
                } else if (passstr.isEmpty()) {
                    pass.setError("Enter Password");
                    pass.requestFocus();
                } else if (emailstr.isEmpty() && passstr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
                } else if (!(emailstr.isEmpty() && passstr.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intToHome = new Intent(login.this, MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(login.this, signUp.class);
                startActivity(signup);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
