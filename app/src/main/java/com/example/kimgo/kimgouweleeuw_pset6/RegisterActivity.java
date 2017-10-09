package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password; 
    String confirmPassword;
    EditText emailText;
    EditText passwordText;
    EditText passwordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText) findViewById(R.id.editEmail);
        passwordText = (EditText) findViewById(R.id.editPassword);
        passwordConfirm = (EditText) findViewById(R.id.editPassword2);

        emailText.setHint("Email");
        passwordText.setHint("Password");
        passwordConfirm.setHint("Confirm password");

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerButton).setOnClickListener(new registerUser());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Signed out", "onAuthStateChanged:signed_out");
                }
            }
        };
    }


    /* Lifecycle methods. */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Create user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Created user: " + email + "successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private class registerUser implements View.OnClickListener {
        @Override public void onClick(View view) {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            confirmPassword = passwordConfirm.getText().toString();
            if (!email.isEmpty() & !password.isEmpty() & !confirmPassword.isEmpty()) {
                if (password.length() >= 6 & password.equals(confirmPassword)) {
                    createUser();
                    Intent intent = new Intent(view.getContext(), SecondActivity.class);
                    startActivity(intent);
                    finish();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Password needs to be at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
