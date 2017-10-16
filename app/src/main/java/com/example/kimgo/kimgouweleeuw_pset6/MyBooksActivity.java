package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MyBooksActivity extends AppCompatActivity {
    MyBooksActivity myBooksAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        myBooksAct = this;

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
        findViewById(R.id.readButton).setOnClickListener(new goToAlreadyRead());
        findViewById(R.id.toReadButton).setOnClickListener(new goToWantToRead());
    }

    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(myBooksAct, MainActivity.class));
        }
    }

    private class goToAlreadyRead implements View.OnClickListener {
        @Override public void onClick(View view) {
            Intent readIntent = new Intent(myBooksAct, ShowListsActivity.class);
            readIntent.putExtra("list", "read");
            startActivity(readIntent);
            finish();
        }
    }

    private class goToWantToRead implements View.OnClickListener {
        @Override public void onClick(View view) {
            Intent toReadIntent = new Intent(myBooksAct, ShowListsActivity.class);
            toReadIntent.putExtra("list", "toread");
            startActivity(toReadIntent);
            finish();
        }
    }
}
