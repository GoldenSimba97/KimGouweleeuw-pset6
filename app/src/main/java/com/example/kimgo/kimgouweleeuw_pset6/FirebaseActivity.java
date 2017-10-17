/*
 * FirebaseActivity. This activity is used to use the Firebase
 * listeners in all activities.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();
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

}
