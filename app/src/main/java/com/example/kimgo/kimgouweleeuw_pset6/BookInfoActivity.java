package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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

import com.google.firebase.auth.FirebaseAuth;

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

        findViewById(R.id.logOutButton).setOnClickListener(new logOut());
    }

    public void bookInfoShow(ArrayList<String> bookInfoArray) {
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
            Intent logOutIntent = new Intent(BookInfoActivity.this, MainActivity.class);
            startActivity(logOutIntent);
        }
    }
}
