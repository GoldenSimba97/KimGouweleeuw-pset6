package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MyBooksActivity extends AppCompatActivity {
    MyBooksActivity myBooksAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        myBooksAct = this;

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
    }

    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(myBooksAct, MainActivity.class));
        }
    }
}
