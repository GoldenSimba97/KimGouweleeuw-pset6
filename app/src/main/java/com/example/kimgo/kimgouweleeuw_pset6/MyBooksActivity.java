/*
 * MyBooksActivity. In this activity users can either click on the
 * Already Read list or the Want To Read list to view the content
 * of these lists.
 *
 * Because this activity extends the ActionbarActivity the user can
 * also click on the search icon to stay in the current activity,
 * click on the heart icon to go to the MyBooksActivity to view the
 * lists with books and click on the log out icon to log out and go
 * back to the MainActivity.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyBooksActivity extends ActionbarActivity {
    private MyBooksActivity myBooksAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        myBooksAct = this;

        findViewById(R.id.readButton).setOnClickListener(new goToAlreadyRead());
        findViewById(R.id.toReadButton).setOnClickListener(new goToWantToRead());
    }


    /* Go to the Already Read list in ShowListsActivity when Already Read button is clicked. */
    private class goToAlreadyRead implements View.OnClickListener {
        @Override public void onClick(View view) {
            Intent readIntent = new Intent(myBooksAct, ShowListsActivity.class);
            readIntent.putExtra("list", "read");
            startActivity(readIntent);
        }
    }


    /* Go to the Want To Read list in ShowListsActivity when Want To Read button is clicked. */
    private class goToWantToRead implements View.OnClickListener {
        @Override public void onClick(View view) {
            Intent toReadIntent = new Intent(myBooksAct, ShowListsActivity.class);
            toReadIntent.putExtra("list", "toread");
            startActivity(toReadIntent);
        }
    }
}
