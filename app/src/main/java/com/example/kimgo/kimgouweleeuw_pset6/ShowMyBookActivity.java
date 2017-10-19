/*
 * ShowMyBookActivity. In this activity users can view extra
 * information about the book they have clicked on in the ShowLists-
 * Activity. If the book is in the Already Read list, the user also
 * has the option to give a rating to the book. The user can also
 * delete the book from their list from here.
 *
 * Because this activity extends the ActionbarActivity the user can
 * also click on the search icon to go to the SecondActivity to search
 * for other books, click on the heart icon to go to the MyBooksActivity
 * to view the lists with books and click on the log out icon to log out
 * and go back to the MainActivity.
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMyBookActivity extends ActionbarActivity {
    private ShowMyBookActivity showMyBookAct;
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
        showMyBookAct = this;

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        book = (Book) getIntent().getExtras().getSerializable("book");
        listType = getIntent().getStringExtra("list");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        showBookInfo();

        setRating();

        findViewById(R.id.deleteButton).setOnClickListener(new DeleteBook());
        findViewById(R.id.deleteButton2).setOnClickListener(new DeleteBook());
    }


    /* Shows all information about the book. The user can also rate the book if it is in the
     * Already Read list. */
    public void showBookInfo() {
        TextView info = (TextView) findViewById(R.id.showBookInfo);
        // Makes TextView scrollable.
        info.setMovementMethod(new ScrollingMovementMethod());

        showRating();

        // Adds all information to a string.
        addToString(book.getTitle());
        addToString(book.getAuthor());
        addToString(book.getPublisher());
        addToString(book.getPublishedDate());

        // Shows the book information, including the html tags, correctly.
        if (book.getDescription().equals("")) {
            info.setText(builder);
        } else {
            SpannableStringBuilder description = new SpannableStringBuilder(getString(
                    R.string.description));
            description.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            info.setText(builder.append(description.append(Html.fromHtml(book.getDescription(),
                    Html.FROM_HTML_MODE_LEGACY))));
        }
    }


    /* Shows the rating of the book when the user is in the Already Read list. */
    public void showRating() {
        if (listType.equals("read")) {
            ratingBar.setVisibility(View.VISIBLE);
            TextView rateIt = (TextView) findViewById(R.id.textRateIt);
            rateIt.setVisibility(View.VISIBLE);

            Button deleteIt = (Button) findViewById(R.id.deleteButton);
            deleteIt.setVisibility(View.VISIBLE);

            Button deleteIt2 = (Button) findViewById(R.id.deleteButton2);
            deleteIt2.setVisibility(View.INVISIBLE);

            float rating = book.getRating();
            if (rating > 0) {
                ratingBar.setRating(rating);
            }
        }
    }


    /* Makes everything until ":" bold and adds to the string. */
    public void addToString(String string) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
        stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                string.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(stringBuilder).append("\n");
    }


    /* Saves the rating the user has given to a book by clicking the number of stars. */
    public void setRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating = ratingBar.getRating();
                book.setRating(rating);
                accessDatabase("rate");
            }
        });
    }


    /* Adds the book object with the changed rating to the database to update the rating if action
     * is equal to "rate" and delete the book object if the action is equal to "delete". */
    public void accessDatabase(final String action) {
        String ID = book.getTitle() + " - " + book.getAuthor();
        final String bookID = ID.replaceAll("\\.", "");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Adds book object to the database if action is "rate" and delete it when
                    // action is "delete".
                    if (action.equals("rate")) {
                        mDatabase.child("books").child(user.getUid()).child(listType).child(bookID)
                                .setValue(book);
                    } else {
                        dataSnapshot.child("books").child(user.getUid()).child(listType)
                                .child(bookID).getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
                Toast.makeText(showMyBookAct, getString(R.string.wrong),
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }


    /* Deletes the book from database when the delete button is clicked and sends user back to
     * the previous activity. */
    private class DeleteBook implements View.OnClickListener {
        @Override public void onClick(View view) {
            accessDatabase("delete");
            finish();
        }
    }

}