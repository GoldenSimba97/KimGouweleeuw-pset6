/*
 * RegisterActivity. In this activity users have the opportunity to
 * register for the GreatReads app. They will be automatically
 * logged in after registering. Firebase is again used to achieve
 * this authentication.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private RegisterActivity registerAct;
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordConfirm;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerAct = this;

        emailText = (EditText) findViewById(R.id.editEmail);
        passwordText = (EditText) findViewById(R.id.editPassword);
        passwordConfirm = (EditText) findViewById(R.id.editPassword2);

        emailText.setHint("Email");
        passwordText.setHint("Password");
        passwordConfirm.setHint("Confirm password");

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        findViewById(R.id.registerButton).setOnClickListener(new registerUser());
    }


    /* Checks if the user is logged in. */
    public void firebaseListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is logged in
                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is logged out
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


    /* Creates new user in Firebase with email and password as authentication. */
    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Create user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If log in fails, display a message to the user. If log in succeeds
                        // the auth state listener will be notified and the newly created user
                        // will receive a message and be sent to the second activity.
                        if (!task.isSuccessful()) {
                            Toast.makeText(registerAct, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(registerAct, "Created user: " + email +
                                    "successfully", Toast.LENGTH_SHORT).show();
                            goToSecondActivity();
                        }
                    }
                });
    }


    /* Registers new user when register button is clicked. This will only happen when all fields
     * have been correctly filled in. When this is not the case a message will be displayed to
     * the user. */
    private class registerUser implements View.OnClickListener {
        @Override public void onClick(View view) {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            String confirmPassword = passwordConfirm.getText().toString();

            if (!email.isEmpty() & !password.isEmpty() & !confirmPassword.isEmpty()) {
                // Check if password is at least 6 characters long and password and confirm
                // password are the same.
                if (password.length() >= 6 & password.equals(confirmPassword)) {
                    createUser();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(registerAct, "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(registerAct, "Password needs to be at least 6 " +
                            "characters long", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /* Goes to SecondActivity. */
    private void goToSecondActivity() {
        startActivity(new Intent(registerAct, SecondActivity.class));
        finish();
    }

}
