/*
 * RegisterActivity. In this activity users have the opportunity to
 * register for the GreatReads app. They will be automatically
 * logged in after registering. Firebase is again used to achieve
 * this authentication.
 *
 * It also extends the FirebaseActivity to be able to use the
 * Firebse listeners.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends FirebaseActivity {
    private RegisterActivity registerAct;
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordConfirm;
    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerButton).setOnClickListener(new RegisterUser());
    }


    /* Creates new user in Firebase with email and password as authentication. */
    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Create user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If create user fails, display a message to the user. If create user
                        // succeeds the auth state listener will be notified and the newly created
                        // user will receive a message and be sent to the SecondActivity.
                        if (!task.isSuccessful()) {
                            Toast.makeText(registerAct, getString(R.string.fail),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(registerAct, getString(R.string.create) + email +
                                    getString(R.string.success), Toast.LENGTH_SHORT).show();
                            goToSecondActivity();
                        }
                    }
                });
    }


    /* Registers new user when register button is clicked. This will only happen when all fields
     * have been correctly filled in. When this is not the case a message will be displayed to
     * the user. */
    private class RegisterUser implements View.OnClickListener {
        @Override public void onClick(View view) {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            String confirmPassword = passwordConfirm.getText().toString();

            if (!email.isEmpty() & !password.isEmpty() & !confirmPassword.isEmpty()) {
                // Checks if password is at least 6 characters long and password and confirm
                // password are the same.
                if (password.length() >= 6 & password.equals(confirmPassword)) {
                    createUser();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(registerAct, getString(R.string.nomatch),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(registerAct, getString(R.string.criteria),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void goToSecondActivity() {
        startActivity(new Intent(registerAct, SecondActivity.class));
        finish();
    }

}