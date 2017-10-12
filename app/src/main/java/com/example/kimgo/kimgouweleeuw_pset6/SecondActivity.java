package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_test";
    SecondActivity secondAct;
    EditText searchBooks;
    ListView lvItems;
    ArrayList<String> idList;
//    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        secondAct = this;

        setListener();

        authTest = FirebaseAuth.getInstance();

        searchBooks = (EditText) findViewById(R.id.searchBooks);
        searchBooks.setHint("Search for books");

        findViewById(R.id.searchButton).setOnClickListener(new bookSearch());
        findViewById(R.id.logOutButton).setOnClickListener(new logOut());

//        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /* Sets the authstate listener. This checks whether there is still a user logged in at
     * the given moment, preventing non-logged in users from seeing pages they shouldn't. */
    private void setListener() {
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextView showUser = (TextView) findViewById(R.id.showUser);
                    Resources res = getResources();
                    String currentUser = res.getString(R.string.hello, user.getEmail());
                    showUser.setText(currentUser);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                    Log.d("logout", "logged out");
                    goToRegisterLoginActivity();
                }
            }
        };
    }


    /* Sends the user back to the starting activity to log in or register. */
    private void goToRegisterLoginActivity() {
        startActivity(new Intent(secondAct, MainActivity.class));
    }


    /* Lifecycle methods. */
    @Override
    public void onStart() {
        super.onStart();
        authTest.addAuthStateListener(authListenerTest);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (authListenerTest != null) {
            authTest.removeAuthStateListener(authListenerTest);
        }
    }

    private class bookSearch implements View.OnClickListener {
        @Override public void onClick(View view) {
            String bookSearch = searchBooks.getText().toString();
            if (!bookSearch.isEmpty()) {
                BookAsyncTask asyncTask = new BookAsyncTask(secondAct);
                asyncTask.execute(bookSearch);

                searchBooks.getText().clear();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    public void bookShow(ArrayList<String> booksArray, ArrayList<String> idArray) {
        idList = idArray;
        TextView results = (TextView)findViewById(R.id.searchResults);
        if (!booksArray.isEmpty()) {
            results.setText(R.string.results);
        } else {
            results.setText(R.string.noresults);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, booksArray);
        lvItems = (ListView) findViewById(R.id.listViewID);
        assert lvItems != null;
        lvItems.setAdapter(arrayAdapter);
        lvItems.setOnItemClickListener(new goToBookInfo());
    }

    private class goToBookInfo implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            String bookID = idList.get(position);
            Log.d("book", bookID);
            Intent intent = new Intent(getApplicationContext(), BookInfoActivity.class);
            intent.putExtra("book", bookID);
            startActivity(intent);
        }
    }


    private class logOut implements View.OnClickListener {
        @Override public void onClick(View view) {
            authTest.signOut();
            Intent logOutIntent = new Intent(secondAct, MainActivity.class);
            startActivity(logOutIntent);
        }
    }


//    public void addToDatabase(View view) {
//        EditText et1 = (EditText) findViewById(R.id.editText);
//        EditText et2 = (EditText) findViewById(R.id.editText2);
//
//        // Get values from edittexts
//        String name = et1.getText().toString();
//        String color = et2.getText().toString();
//
//        // Add an object to the database
//        Fruit aFruit = new Fruit(name, color);
//
//        mDatabase.child("fruitbasket").child("fruit1").setValue(aFruit);
//    }
//
//
//    // kan ook read data once gebruiken
//    public void getFromDatabase(View view) {
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get object out of database
//                Fruit aFruit = dataSnapshot.child("fruitbasket").child("fruit1").getValue(Fruit.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mDatabase.addValueEventListener(postListener);
//    }
}
