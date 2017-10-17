/*
 * ShowListsActivity. In this activity users are able to view the
 * content of either their Already Read list or their Want To Read
 * list. They can delete books from these lists on a long click and
 * view extra information about the book when they click on the book.
 * If the book is in the Already Read list, the user can give a rating
 * to the book. If the book is in the Want To Read list, the user
 * can move it to the Already Read list on a long click.
 *
 * Because this activity extends the ActionbarActivity the user can
 * also click on the search icon to stay in the current activity,
 * click on the heart icon to go to the MyBooksActivity to view the
 * lists with books and click on the log out icon to log out and go
 * back to the MainActivity.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowListsActivity extends ActionbarActivity {
    private ShowListsActivity showListsAct;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String list;
    private TextView showList;
    private ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);
        showListsAct = this;

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = getIntent().getStringExtra("list");

        showList = (TextView) findViewById(R.id.myBookListText);
        lvItems = (ListView) findViewById(R.id.myBooksView);

        showBooksList();

        lvItems.setOnItemLongClickListener(new deleteBook());
        lvItems.setOnItemClickListener(new showBook());
    }


    /* Get all books saved in the database for this list from the database and add them to the
     * arraylist and show this list as a listview. */
    public void getFromDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                ArrayList<String> booksList = new ArrayList<>();
                assert user != null;
                // Get Book object out of database
                DataSnapshot myBooks = dataSnapshot.child("books").child(user.getUid()).child(list);
                for (DataSnapshot myBook: myBooks.getChildren()) {
                    Book book = myBook.getValue(Book.class);
                    String title = book.getTitle();
                    String author = book.getAuthor();
                    title = title.replace(title.substring(0, title.indexOf(":") + 2), "");
                    author = author.replace(author.substring(0, author.indexOf(":") + 2), "");
                    booksList.add(title + " - " + author);
                }
                bookListAdapter(booksList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }


    /* Show the title of the list and the list with books beneath that. */
    public void showBooksList() {
        Resources res = getResources();
        if (list.equals("read")) {
            showList.setText(res.getString(R.string.read));
        } else {
            showList.setText(res.getString(R.string.toread));
        }
        getFromDatabase();
    }


    /* Adapter for showing the books in a listview. */
    public void bookListAdapter(ArrayList<String> booksList) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, booksList);
        assert lvItems != null;
        lvItems.setAdapter(arrayAdapter);
    }


    /* Access the database to either retrieve more information about the book, remove the book
     * from the database or move the book from the Want To Read list to the Already Read list. */
    public void accessDatabase(final String bookID, final String action) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                // Get Book object out of database
                DataSnapshot myBooks = dataSnapshot.child("books").child(user.getUid()).child(list);
                Book myBook = myBooks.child(bookID).getValue(Book.class);

                switch (action) {
                    // Retrieve the Book object to retrieve more information
                    case("view"):
                        Intent intent = new Intent(showListsAct, ShowMyBookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("book", myBook);
                        intent.putExtras(bundle);
                        intent.putExtra("list", list);
                        startActivity(intent);
                        break;
                    // Delete Book object from database.
                    case("delete"):
                        myBooks.child(bookID).getRef().removeValue();
                        break;
                    // Move Book object to Already Read list.
                    case("move"):
                        myBooks.child(bookID).getRef().removeValue();
                        mDatabase.child("books").child(user.getUid()).child("read").child(bookID)
                                .setValue(myBook);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }


    /* When a book in the listview is clicked access the book object in the database and go to the
     * ShowMyBookActivity where more information about the book will be displayed. */
    private class showBook implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            String book = ((TextView) view).getText().toString();

            book = new StringBuilder(book).insert(book.indexOf("-") + 2, "Author: ").
                    insert(0, "Title: ").toString();
            String bookID = book.replaceAll("\\.", "");
            accessDatabase(bookID, "view");
        }
    }


    /* Delete book from database (or move to Already Read list) when it is long clicked. */
    private class deleteBook implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            String book = ((TextView) view).getText().toString();
            book = new StringBuilder(book).insert(book.indexOf("-") + 2, "Author: ")
                    .insert(0, "Title: ").toString();
            String bookID = book.replaceAll("\\.", "");

            showPopUp(bookID, "delete");
            getFromDatabase();
            return true;
        }
    }


    /* Show alertdialog which asks the user if he wants to delete the book. If the user is viewing
     * the Want To Read list he also has the option to move the book to the Already Read list. */
    private void showPopUp(final String book, final String delete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(showListsAct);
        builder.setCancelable(true);

        builder.setPositiveButton("Delete this book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accessDatabase(book, delete);
            }
        });

        if (list.equals("toread")) {
            builder.setNegativeButton("Move to Already Read list", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    accessDatabase(book, "move");
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
