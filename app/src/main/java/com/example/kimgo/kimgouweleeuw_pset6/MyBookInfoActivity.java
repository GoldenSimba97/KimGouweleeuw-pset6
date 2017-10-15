package com.example.kimgo.kimgouweleeuw_pset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyBookInfoActivity extends AppCompatActivity {
    MyBookInfoActivity myBookInfoAct;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_info);

        myBookInfoAct = this;

        book = (Book) getIntent().getExtras().getSerializable("book");

        showBookInfo();
    }

    public void showBookInfo() {
//        TextView info = (TextView)findViewById(R.id.showBookInfo);
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

        Integer rank = book.getRank();
        if (rank > 0) {
            RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
            ratingBar.setRating(rank);
        }

        SpannableStringBuilder description = new SpannableStringBuilder("Book description: ");
//        description.append(book.getDescription());
        description.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        info.setText(builder.append(description.append(Html.fromHtml(book.getDescription(), Html.FROM_HTML_MODE_LEGACY))));

    }



    //    public void bookInfoShow(ArrayList<String> bookInfoArray) {
//        allBookInfo = bookInfoArray;
//        TextView info = (TextView)findViewById(R.id.showBookInfo);
//        TextView description = (TextView)findViewById(R.id.showBookDescription);
//        description.setMovementMethod(new ScrollingMovementMethod());
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        for (int i = 0; i < bookInfoArray.size() - 1; ++i) {
//            String bookInfo = bookInfoArray.get(i);
//            SpannableStringBuilder information = new SpannableStringBuilder(bookInfo);
//            information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, bookInfo.indexOf(":") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            builder.append(information).append("\n");
//        }
//        SpannableStringBuilder information = new SpannableStringBuilder("Book description: ");
//        information.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        info.setText(builder);
//        description.setText(information.append(Html.fromHtml(bookInfoArray.get(bookInfoArray.size() - 1), Html.FROM_HTML_MODE_LEGACY)));
//    }
}
