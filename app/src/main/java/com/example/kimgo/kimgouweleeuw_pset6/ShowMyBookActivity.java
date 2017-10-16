package com.example.kimgo.kimgouweleeuw_pset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMyBookActivity extends ActionbarActivity {
    Book book;
    String listType;
    SpannableStringBuilder builder = new SpannableStringBuilder();
    RatingBar ratingBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_book);

        Log.d("emsee", "activity");

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        book = (Book) getIntent().getExtras().getSerializable("book");
        listType = getIntent().getStringExtra("list");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        showBookInfo();

        setRating();
    }


    public void showBookInfo() {
        TextView info = (TextView) findViewById(R.id.showBookInfo);
        info.setMovementMethod(new ScrollingMovementMethod());

        float rating = book.getRating();
        if (rating > 0) {
            ratingBar.setRating(rating);
        }

        addToString(book.getTitle());
        addToString(book.getAuthor());
        addToString(book.getPublisher());
        addToString(book.getPublishedDate());

        SpannableStringBuilder description = new SpannableStringBuilder("Book description: ");
        description.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        info.setText(builder.append(description.append(Html.fromHtml(book.getDescription(), Html.FROM_HTML_MODE_LEGACY))));
    }


    public void addToString(String string) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
        stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, string.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(stringBuilder).append("\n");
    }


    public void setRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating = ratingBar.getRating();
                book.setRating(rating);
                addToDatabase();
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
}
