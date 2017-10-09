package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_test";
//    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        authTest = FirebaseAuth.getInstance();
        setListener();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /* Sets the authstate listener. This checks whether there is still a user logged in at
     * the given moment, preventing non-logged in users from seeing pages they shouldn't. */
    private void setListener() {
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextView showUser = (TextView) findViewById(R.id.showUser);
                    showUser.setText(user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                    goToRegisterLoginActivity();
                }
            }
        };
    }


    /* Sends the user back to the starting activity to log in or register. */
    private void goToRegisterLoginActivity() {
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
    }


    /* Lifecycle methods. */
    @Override
    public void onStart() {
        super.onStart();
        authTest.addAuthStateListener(authListenerTest);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (authListenerTest != null) {
            authTest.removeAuthStateListener(authListenerTest);
        }
    }


//    public void addToDatabase(View view) {
//        EditText et1 = (EditText) findViewById(R.id.editText);
//        EditText et2 = (EditText) findViewById(R.id.editText2);
//
//        // Get values from edittexts
//        String name = et1.getText().toString();
//        String color = et2.getText().toString();
//
//        // Add an object to the database
//        Fruit aFruit = new Fruit(name, color);
//
//        mDatabase.child("fruitbasket").child("fruit1").setValue(aFruit);
//    }
//
//
//    // kan ook read data once gebruiken
//    public void getFromDatabase(View view) {
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get object out of database
//                Fruit aFruit = dataSnapshot.child("fruitbasket").child("fruit1").getValue(Fruit.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mDatabase.addValueEventListener(postListener);
//    }
}
