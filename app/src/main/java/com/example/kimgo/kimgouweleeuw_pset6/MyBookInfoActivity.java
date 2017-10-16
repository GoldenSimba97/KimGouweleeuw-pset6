package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.String.valueOf;

public class MyBookInfoActivity extends AppCompatActivity {
    MyBookInfoActivity myBookInfoAct;
    Book book;
    String listType;
    RatingBar ratingBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_info);

        myBookInfoAct = this;

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        book = (Book) getIntent().getExtras().getSerializable("book");
        listType = getIntent().getStringExtra("list");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        showBookInfo();

        setRating();

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
        findViewById(R.id.myBooksButton).setOnClickListener(new goToMyBooks());
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

    public void showBookInfo() {
        TextView info = (TextView)findViewById(R.id.showBookInfo);
        info.setMovementMethod(new ScrollingMovementMethod());
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableStringBuilder title = new SpannableStringBuilder(book.getTitle());
        title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, book.getTitle().indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(title).append("\n");

        SpannableStringBuilder author = new SpannableStringBuilder(book.getAuthor());
        author.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, book.getAuthor().indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(author).append("\n");

        SpannableStringBuilder publisher = new SpannableStringBuilder(book.getPublisher());
        publisher.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, book.getPublisher().indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(publisher).append("\n");

        SpannableStringBuilder publishedDate = new SpannableStringBuilder(book.getPublishedDate());
        publishedDate.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, book.getPublishedDate().indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(publishedDate).append("\n");

        float rating = book.getRating();
        if (rating > 0) {
            ratingBar.setRating(rating);
        }

        SpannableStringBuilder description = new SpannableStringBuilder("Book description: ");
        description.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        info.setText(builder.append(description.append(Html.fromHtml(book.getDescription(), Html.FROM_HTML_MODE_LEGACY))));

    }

    public void setRating() {
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating = ratingBar.getRating();
//                Log.d("rating", String.format("%f", rating));
                book.setRating(rating);
//                float bookRating = book.getRating();
//                Log.d("rating", String.format("%f", bookRating));
//                ratingBar.setRating(rating);
                addToDatabase();
//                Log.d("hey", "hey");

            }
        });
    }



    public void addToDatabase() {
        // Ad an object to the database
        String ID = book.getTitle() + " - " + book.getAuthor();
        final String bookID = ID.replaceAll("\\.", "");
        Log.d("id", bookID);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object out of database
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                mDatabase.child("books").child(user.getUid()).child(listType).child(bookID).setValue(book);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }


    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(myBookInfoAct, MainActivity.class));
        }
    }


    private class goToMyBooks implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(myBookInfoAct, MyBooksActivity.class));
        }
    }


}
