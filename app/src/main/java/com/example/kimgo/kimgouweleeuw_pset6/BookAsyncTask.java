package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kimgo on 10-10-2017.
 */

public class BookAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private SecondActivity secondAct;
    private BookInfoActivity bookAct;
    private int type;

    BookAsyncTask(SecondActivity second) {
        this.secondAct = second;
        this.context = this.secondAct.getApplicationContext();
        this.type = 1;
    }

    BookAsyncTask(BookInfoActivity book) {
        this.bookAct = book;
        this.context = this.bookAct.getApplicationContext();
        this.type = 2;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Searching...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(type, params);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        String title = "";
        String author = "";
//        Integer types;
//        types = type;
//        Log.d("title", types.toString());

        try {
            JSONObject bookStreamObj = new JSONObject(result);
            if (type == 1) {
                JSONArray resultObj = bookStreamObj.getJSONArray("items");
                for (int i = 0; i < resultObj.length(); ++i) {
                    JSONObject book = resultObj.getJSONObject(i);
                    String id = book.getString("id");
                    JSONObject volumeObj = book.getJSONObject("volumeInfo");
                    title = volumeObj.getString("title");
                    JSONArray authorObj = volumeObj.getJSONArray("authors");
                    author = authorObj.getString(0);
                    list.add(title + " - " + author);
                    idList.add(id);
                }
            } else {
                JSONObject volumeObj = bookStreamObj.getJSONObject("volumeInfo");
                title = volumeObj.getString("title");
                JSONArray authorObj = volumeObj.getJSONArray("authors");
                author = authorObj.getString(0);
                String publisher = volumeObj.getString("publisher");
                String publishedDate = volumeObj.getString("publishedDate");
                String description = volumeObj.getString("description");
                list.add("Title: " + title);
                list.add("Author: " + author);
                list.add("Publisher: " + publisher);
                list.add("Publication date: " + publishedDate);
                list.add(description);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type == 1) {
            this.secondAct.bookShow(list, idList);
        } else {
            this.bookAct.bookInfoShow(list);
        }
    }

}
