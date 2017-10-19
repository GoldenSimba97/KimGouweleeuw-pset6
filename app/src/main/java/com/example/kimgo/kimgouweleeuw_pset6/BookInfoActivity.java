/*
 * BookInfoActivity. In this activity users are able to view extra
 * information about the book they have selected in the SecondActivity.
 * Users can add the book to either the Already Read list or the Want
 * To Read list.
 *
 * Because this activity extends the ActionbarActivity the user can
 * also click on the search icon to stay in the current activity,
 * click on the heart icon to go to the MyBooksActivity to view the
 * lists with books and click on the log out icon to log out and go
 * back to the MainActivity.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookInfoActivity extends ActionbarActivity {
    private BookInfoActivity bookAct;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> allBookInfo;
    private SpannableStringBuilder builder = new SpannableStringBuilder();
    private Book book;
    private String bookID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        bookAct = this;

        // Gets the extra information about the book clicked in the SecondActivity
        String bookID = getIntent().getStringExtra("book");
        BookAsyncTask asyncTask = new BookAsyncTask(bookAct);
        asyncTask.execute(bookID);

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.addReadButton).setOnClickListener(new AddToAlreadyRead());
        findViewById(R.id.addToReadButton).setOnClickListener(new AddToWantToRead());
    }


    /* Shows all information about the book the user has clicked in the SecondActivity by appending
     * all information into a single string. */
    public void bookInfoShow(ArrayList<String> bookInfoArray) {
        allBookInfo = bookInfoArray;
        TextView info = (TextView)findViewById(R.id.showBookInfo);
        // Makes TextView scrollable to be able to view all information if it is too much to fit
        // on the screen.
        info.setMovementMethod(new ScrollingMovementMethod());

        // Makes "Title: ", "Author: ", "Publisher: " (and "Publication data: " if size of
        // bookinfoArray is 5) bold.
        for (int i = 0; i < bookInfoArray.size() - 1; ++i) {
            addToString(bookInfoArray.get(i));
        }

        // Makes "Publication date: " bold if size of bookInfoArray is less than 5 and shows all
        // info. Else also makes "Book description: " bold and correctly shows all info (including
        // html tags in the description).
        if (bookInfoArray.size() < 5) {
            addToString(bookInfoArray.get(3));
            info.setText(builder);
        } else {
            SpannableStringBuilder information = new SpannableStringBuilder(getString(
                    R.string.description));
            information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            info.setText(builder.append(information).append(Html.fromHtml(
                    bookInfoArray.get(bookInfoArray.size() - 1), Html.FROM_HTML_MODE_LEGACY)));
        }
    }


    /* Makes everything until ":" bold and adds to the string. */
    public void addToString(String bookInfo) {
        SpannableStringBuilder information = new SpannableStringBuilder(bookInfo);
        information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                bookInfo.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(information).append("\n");
    }


    /* Adds book to Already Read list in the database when Already Read button is clicked. */
    private class AddToAlreadyRead implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            addToDatabase("read");
        }
    }


    /* Adds book to Want To Read list in the database when Want To Read button is clicked. */
    private class AddToWantToRead implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            addToDatabase("toread");
        }
    }


    /* Adds the book object to the database if it doesn't already exist in either the Already Read
     * list or the Want To Read list. A message will be displayed to the user about the success or
     * failure of the task. */
    public void addToDatabase(final String listType) {
        getBookAndBookID();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                DataSnapshot myBooks = dataSnapshot.child("books").child(user.getUid());

                // Checks if book already exists somewhere in the database
                if (myBooks.child("read").child(bookID).exists()) {
                    Toast.makeText(bookAct, getString(R.string.alreadyfail),
                                Toast.LENGTH_SHORT).show();
                } else if (myBooks.child("toread").child(bookID).exists()) {
                    Toast.makeText(bookAct, getString(R.string.wantfail),
                                Toast.LENGTH_SHORT).show();
                } else {
                    mDatabase.child("books").child(user.getUid()).child(listType).child(bookID)
                            .setValue(book);
                    if (listType.equals("read")) {
                        Toast.makeText(bookAct, getString(R.string.alreadysuccess),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(bookAct, getString(R.string.wantsuccess),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }


    /* Uses all the book information to create a Book object and title and author to create
     * the bookID for the database. */
    public void getBookAndBookID() {
        if (allBookInfo.size() < 5) {
            book = new Book(allBookInfo.get(0), allBookInfo.get(1), allBookInfo.get(2),
                    allBookInfo.get(3));
        } else {
            book = new Book(allBookInfo.get(0), allBookInfo.get(1), allBookInfo.get(2),
                    allBookInfo.get(3), allBookInfo.get(4));
        }
        String id = allBookInfo.get(0) + " - " + allBookInfo.get(1);
        bookID = id.replaceAll("\\.", "");
    }

}