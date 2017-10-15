package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    ShowListsActivity showListsAct;
    ListView lvItems;
    ArrayList<Book> booksArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);

        showListsAct = this;

        mAuth = FirebaseAuth.getInstance();

        firebaseListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = getIntent().getStringExtra("list");
        showList = (TextView) findViewById(R.id.myBookListText);

        showBooksList();

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
        findViewById(R.id.myBooksButton).setOnClickListener(new goToMyBooks());
//        lvItems.setOnItemClickListener(new goToTodo());
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
                FirebaseUser user = mAuth.getCurrentUser();
                ArrayList<String> booksList = new ArrayList<>();
                assert user != null;
                DataSnapshot myBooks = dataSnapshot.child("books").child(user.getUid()).child(list);
                for (DataSnapshot myBook: myBooks.getChildren()) {
                    Book book = myBook.getValue(Book.class);
                    booksArray.add(book);
                    String title = book.getTitle();
                    String author = book.getAuthor();
                    title = title.replace(title.substring(0, title.indexOf(":") + 2), "");
                    author = author.replace(author.substring(0, author.indexOf(":") + 2), "");
                    booksList.add(title + " - " + author);
                    bookListAdapter(booksList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("read_failed", "The read failed: " + databaseError.getCode());
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
        lvItems = (ListView) findViewById(R.id.myBooksView);
        assert lvItems != null;
        lvItems.setAdapter(arrayAdapter);
//        lvItems.setOnItemClickListener(new SecondActivity.goToBookInfo());

    }


    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(showListsAct, MainActivity.class));
        }
    }


    private class goToMyBooks implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(showListsAct, MyBooksActivity.class));
        }
    }


//    private class goToTodo implements AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view,
//                                int position, long id) {
//            String book = ((TextView) view).getText().toString();
//
//            Intent intent = new Intent(view.getContext(), SecondActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("listTitle", toDo.getTitle());
//            bundle.putInt("listID", toDo.getID());
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//    }
}