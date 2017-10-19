/*
 * MainActivity. In this activity users have the opportunity to
 * either log in or go to the RegisterActivity to register for
 * the GreatReads app. This authentication is done using Firebase.
 *
 * It also extends the FirebaseActivity to be able to use the
 * Firebase listeners.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends FirebaseActivity {
    private MainActivity mainAct;
    private FirebaseAuth mAuth;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAct = this;

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logInButton).setOnClickListener(new LogInUser());

        goToRegisterUser();
    }


    /* Logs in and authenticates user in Firebase with email and password. */
    public void logIn() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Log in", "logInWithEmail:onComplete:" + task.isSuccessful());

                        // If log in fails, display a message to the user. If log in succeeds
                        // the auth state listener will be notified and the logged in user will
                        // receive a message and be sent to the SecondActivity.
                        if (!task.isSuccessful()) {
                            Log.w("Email", "logInWithEmail:failed", task.getException());
                            Toast.makeText(mainAct, getString(R.string.authfail),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Success", "logged in");
                            Toast.makeText(mainAct, getString(R.string.loggedin) + email +
                                    getString(R.string.success), Toast.LENGTH_SHORT).show();
                            goToSecondActivity();
                        }
                    }
                });
    }


    /* Logs in user when log in button is clicked and email and password are filled in. */
    private class LogInUser implements View.OnClickListener {
        @Override public void onClick(View view) {
            EditText emailText = (EditText) findViewById(R.id.editEmail);
            EditText passwordText = (EditText) findViewById(R.id.editPassword);
            email = emailText.getText().toString();
            password = passwordText.getText().toString();

            if (!email.isEmpty() & !password.isEmpty()) {
                logIn();
            }
        }
    }


    private void goToSecondActivity() {
        startActivity(new Intent(mainAct, SecondActivity.class));
    }


    /* Goes to RegisterActivity to register a new user when "here" is clicked. */
    public void goToRegisterUser() {
        TextView register = (TextView) findViewById(R.id.registerText);
        String registerHere = getString(R.string.noaccount);

        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setText(registerHere, TextView.BufferType.SPANNABLE);

        // Make "here" clickable.
        Spannable mySpannable = (Spannable)register.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainAct, RegisterActivity.class));
                finish();
            }
        };
        mySpannable.setSpan(myClickableSpan, 36, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}