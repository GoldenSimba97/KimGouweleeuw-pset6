package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowListsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    String list;
    TextView showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = getIntent().getStringExtra("list");
        showList = (TextView) findViewById(R.id.myBookListText);

        showBooksList();
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

    public void getFromDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object out of database
//                String key = dataSnapshot.getKey();
//                Log.d("key", key);
                FirebaseUser user = mAuth.getCurrentUser();
                ArrayList<String> booksList = new ArrayList<>();
                assert user != null;
//                Book book = dataSnapshot.child("books").child(user.getUid()).child(list).getValue(Book.class);
                DataSnapshot myBooks = dataSnapshot.child("books").child(user.getUid()).child(list);
//                Log.d("data", myBooks.getValue().toString());
//                Log.d("data", myBooks.getKey());
                for (DataSnapshot myBook: myBooks.getChildren()) {
                    Book book = myBook.getValue(Book.class);
//                    Log.d("data", book.getTitle());
                    String title = book.getTitle();
                    String author = book.getAuthor();
                    int endIndex = title.indexOf(":");
                    int endIndex2 = author.indexOf(":");
                    title = title.replace(title.substring(0, title.indexOf(":") + 2), "");
                    author = author.replace(author.substring(0, author.indexOf(":") + 2), "");
                    booksList.add(title + " - " + author);
                    bookListAdapter(booksList);
                }
//                String title = book.getTitle();
//                String author = book.getAuthor();
//                booksList.add(title + " - " + author);
//                bookListAdapter(booksList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowListsActivity.this, "The read failed: " + databaseError.getCode(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }


    public void showBooksList() {
        Resources res = getResources();
        if (list.equals("read")) {
            showList.setText(res.getString(R.string.read));
        } else {
            showList.setText(res.getString(R.string.toread));
        }
        getFromDatabase();
    }


    public void bookListAdapter(ArrayList<String> booksList) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, booksList);
        ListView lvItems = (ListView) findViewById(R.id.myBooksView);
        assert lvItems != null;
        lvItems.setAdapter(arrayAdapter);
//        lvItems.setOnItemClickListener(new SecondActivity.goToBookInfo());

    }
}
