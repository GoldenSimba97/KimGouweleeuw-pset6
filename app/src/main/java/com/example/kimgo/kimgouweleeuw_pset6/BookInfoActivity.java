package com.example.kimgo.kimgouweleeuw_pset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookInfoActivity extends AppCompatActivity {
    BookInfoActivity bookAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        bookAct = this;

        String bookID = getIntent().getStringExtra("book");
        BookAsyncTask asyncTask = new BookAsyncTask(bookAct);
        asyncTask.execute(bookID);
    }

    public void bookInfoShow(ArrayList<String> bookInfoArray) {
        TextView info = (TextView)findViewById(R.id.showBookInfo);
        Integer length = bookInfoArray.size();
        Log.d("length", length.toString());
//        info.setText(bookInfoArray.get(0));
        StringBuilder builder = new StringBuilder();
        for (String details : bookInfoArray) {
            builder.append(details).append("\n");
        }

        info.setText(builder.toString());
    }
}
