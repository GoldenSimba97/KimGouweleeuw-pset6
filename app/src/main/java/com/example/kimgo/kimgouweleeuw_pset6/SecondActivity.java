/*
 * SecondActivity. This is the activity where users that have just
 * logged in or registered end up. In this activity users have the
 * opportunity to search for books and click on the resulting books
 * to get more information about the book. If something went wrong
 * and the user isn't actually logged in, he will be sent back to
 * the MainActivity.
 *
 * Because this activity extends the ActionbarActivity the user can
 * also click on the search icon to stay in the current activity,
 * click on the heart icon to go to the MyBooksActivity to view the
 * lists with books and click on the log out icon to log out and go
 * back to the MainActivity.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SecondActivity extends ActionbarActivity {
    private SecondActivity secondAct;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Firebase_test";
    private ArrayList<String> idList;
    private ArrayList<String> bookResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        secondAct = this;

        mAuth = FirebaseAuth.getInstance();

        setListener();

        findViewById(R.id.searchButton).setOnClickListener(new BookSearch());
    }


    /* Sets the authstate listener. This checks whether there is still a user logged in at
     * the given moment, preventing non-logged in users from seeing pages they shouldn't. */
    private void setListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is logged in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextView showUser = (TextView) findViewById(R.id.showUser);
                    Resources res = getResources();
                    String currentUser = res.getString(R.string.hello, user.getEmail());
                    showUser.setText(currentUser);
                } else {
                    // User is logged out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                    goToRegisterLoginActivity();
                }
            }
        };
    }


    private void goToRegisterLoginActivity() {
        startActivity(new Intent(secondAct, MainActivity.class));
        finish();
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


    /* Creates bundle when onSaveInstanceState is called to save the search results. */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("bookResults", bookResults);
        outState.putStringArrayList("bookID", idList);
    }


    /* Gets the saved search results to show them when the instance state is restored. */
    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        bookResults = inState.getStringArrayList("bookResults");
        idList = inState.getStringArrayList("bookID");
        bookAdapter();
    }


    /* Searches for the books of the specific search query the user has typed in when the
     * search button is clicked. This only happens when the user has actually typed something
     * in the search bar. */
    private class BookSearch implements View.OnClickListener {
        @Override public void onClick(View view) {
            EditText searchBooks = (EditText) findViewById(R.id.searchBooks);
            String bookSearch = searchBooks.getText().toString();

            // Checks whether the user has typed in a search query before clicking the search button
            if (!bookSearch.isEmpty()) {
                BookAsyncTask asyncTask = new BookAsyncTask(secondAct);
                asyncTask.execute(bookSearch);

                searchBooks.getText().clear();

                // Removes the keyboard so the output of the search query can be seen better.
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /* Displays all books that have been found matching the search query in a ListView. If no
     * search results have been found, a message will be displayed for the user. */
    public void bookShow(ArrayList<String> booksArray, ArrayList<String> idArray) {
        bookResults = booksArray;
        // Save the id's of the books to be able to ask for more information
        idList = idArray;
        bookAdapter();
    }


    /* Puts all search results in the ListView and displays a message. */
    public void bookAdapter() {
        TextView results = (TextView) findViewById(R.id.searchResults);
        // Checks if any search results have been found
        if (!bookResults.isEmpty()) {
            results.setText(R.string.results);
        } else {
            results.setText(R.string.noresults);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, bookResults);
        ListView lvItems = (ListView) findViewById(R.id.listViewID);
        assert lvItems != null;
        lvItems.setAdapter(arrayAdapter);
        lvItems.setOnItemClickListener(new GoToBookInfo());
    }


    /* When a book in the ListView is clicked go to the BookInfoActivity where more information
     * about the book will be displayed. */
    private class GoToBookInfo implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            String bookID = idList.get(position);
            Intent intent = new Intent(secondAct, BookInfoActivity.class);
            intent.putExtra("book", bookID);
            startActivity(intent);
        }
    }

}