/*
 * ActionbarActivity. In this activity the action bar is made.
 * When the user clicks on the search icon he will be navigated to
 * the SecondActivity where he can search for books. When the user
 * clicks on the heart icon he will be navigated to the MyBooks-
 * Activity where he will be able to view the lists with books.
 * And when the user clicks on the log out icon he will log out
 * and go back to the MainActivity.
 *
 * It also extends the FirebaseActivity to be able to use the
 * Firebse listeners.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActionbarActivity extends FirebaseActivity {
    private ActionbarActivity actionbarAct;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar);
        actionbarAct = this;

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            // Go to the SecondActivity when the search icon is clicked.
            case R.id.action_search:
                startActivity(new Intent(actionbarAct, SecondActivity.class));
                return(true);
            // Go to the MyBooksActivity when the heart icon is clicked.
            case R.id.action_mybooks:
                startActivity(new Intent(actionbarAct, MyBooksActivity.class));
                return(true);
            // Go to the MainActivity when the log out icon is clicked.
            case R.id.action_logout:
                mAuth.signOut();
                startActivity(new Intent(actionbarAct, MainActivity.class));
                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

}
