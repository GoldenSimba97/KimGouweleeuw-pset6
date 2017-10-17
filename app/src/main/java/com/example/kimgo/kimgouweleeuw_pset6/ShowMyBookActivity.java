/*
 * ShowMyBookActivity. In this activity users can view extra
 * information about the book they have clicked on in the ShowLists-
 * Activity. If the book is in the Already Read list, the user also
 * has the option to give a rating to the book.
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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Book book;
    private String listType;
    private RatingBar ratingBar;
    private SpannableStringBuilder builder = new SpannableStringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_book);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        book = (Book) getIntent().getExtras().getSerializable("book");
        listType = getIntent().getStringExtra("list");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        showBookInfo();

        setRating();
    }


    /* Show all information about the book. The user can also rate the book if it is in the
     * Already Read list. */
    public void showBookInfo() {
        TextView info = (TextView) findViewById(R.id.showBookInfo);
        // Make textview scrollable.
        info.setMovementMethod(new ScrollingMovementMethod());

        // Show rating of book when in Already Read list.
        if (listType.equals("read")) {
            ratingBar.setVisibility(View.VISIBLE);
            TextView rateIt = (TextView) findViewById(R.id.textRateIt);
            rateIt.setVisibility(View.VISIBLE);
            float rating = book.getRating();
            if (rating > 0) {
                ratingBar.setRating(rating);
            }
        }

        // Add all information to a string.
        addToString(book.getTitle());
        addToString(book.getAuthor());
        addToString(book.getPublisher());
        addToString(book.getPublishedDate());

        // Correctly show the book information, including the html tags.
        if (book.getDescription().equals("")) {
            info.setText(builder);
        } else {
            SpannableStringBuilder description = new SpannableStringBuilder("Book description: ");
            description.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            info.setText(builder.append(description.append(Html.fromHtml(book.getDescription(),
                    Html.FROM_HTML_MODE_LEGACY))));
        }
    }


    /* Make everything until ":" bold and add to the string. */
    public void addToString(String string) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
        stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                string.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(stringBuilder).append("\n");
    }


    /* When the user has rated the book by clicking the number of stars it will be saved in the
     * database. */
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


    /* Add the Book object with the changed rating to the database to update the rating. */
    public void addToDatabase() {
        String ID = book.getTitle() + " - " + book.getAuthor();
        final String bookID = ID.replaceAll("\\.", "");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                // Add Book object to the database.
                mDatabase.child("books").child(user.getUid()).child(listType).child(bookID)
                        .setValue(book);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

}
