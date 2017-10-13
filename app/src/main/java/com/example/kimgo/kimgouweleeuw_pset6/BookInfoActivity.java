package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookInfoActivity extends AppCompatActivity {
    BookInfoActivity bookAct;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    ArrayList<String> allBookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        bookAct = this;

        String bookID = getIntent().getStringExtra("book");
        BookAsyncTask asyncTask = new BookAsyncTask(bookAct);
        asyncTask.execute(bookID);

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
        findViewById(R.id.myBooksButton).setOnClickListener(new goToMyBooks());
        findViewById(R.id.addReadButton).setOnClickListener(new addToAlreadyRead());
        findViewById(R.id.addToReadButton).setOnClickListener(new addToWantToRead());
    }

    public void firebaseListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
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

    public void bookInfoShow(ArrayList<String> bookInfoArray) {
        allBookInfo = bookInfoArray;
        TextView info = (TextView)findViewById(R.id.showBookInfo);
        TextView description = (TextView)findViewById(R.id.showBookDescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < bookInfoArray.size() - 1; ++i) {
            String bookInfo = bookInfoArray.get(i);
            SpannableStringBuilder information = new SpannableStringBuilder(bookInfo);
            information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, bookInfo.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(information).append("\n");
        }
        SpannableStringBuilder information = new SpannableStringBuilder("Book description: ");
        information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        info.setText(builder);
        description.setText(information.append(Html.fromHtml(bookInfoArray.get(bookInfoArray.size() - 1), Html.FROM_HTML_MODE_LEGACY)));
    }

    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(bookAct, MainActivity.class));
        }
    }

    private class goToMyBooks implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(bookAct, MyBooksActivity.class));
        }
    }

//    public void firebaseListener(final String listType) {
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
//                    addToDatabase(listType, user);
//                } else {
//                    // User is signed out
//                    Log.d("Signed out", "onAuthStateChanged:signed_out");
//                }
//            }
//        };
//    }


    private class addToAlreadyRead implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FirebaseUser user = mAuth.getCurrentUser();
            addToDatabase("read", user);
            Toast.makeText(bookAct, "Book added successfully to Already Read list",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class addToWantToRead implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FirebaseUser user = mAuth.getCurrentUser();
            addToDatabase("toread", user);
            Toast.makeText(bookAct, "Book added successfully to Want To Read list",
                    Toast.LENGTH_SHORT).show();
        }
    }



    public void addToDatabase(String listType, FirebaseUser user) {
//        ArrayList<String> info = new ArrayList<>();;
//        for (int i = 0; i < allBookInfo.size() - 1; ++i) {
//            String bookInfo = allBookInfo.get(i);
//            int endIndex = bookInfo.indexOf(":");
//            info.add(bookInfo.replace(bookInfo.substring(0, endIndex + 1), ""));
//        }

        // Ad an object to the database
        Book book = new Book(allBookInfo.get(0), allBookInfo.get(1), allBookInfo.get(2), allBookInfo.get(3), allBookInfo.get(4));

//        DatabaseReference booksRef = mDatabase.child("books");
//
//        booksRef.child(user.getUid()).child(listType).push().setValue(book);

        mDatabase.child("books").child(user.getUid()).child(listType).setValue(book);
        // misschien toch push() gebruiken voor key.
    }
}
