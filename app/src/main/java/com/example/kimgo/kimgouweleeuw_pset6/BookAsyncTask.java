/*
 * BookAsyncTask class. In this class the HttpRequestHelper
 * class will be called to get the Json file which contains
 * all the books that are found for the search query as a
 * string. The needed information about the book is retrieved
 * from this file and added to an arraylist.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class BookAsyncTask extends AsyncTask<String, Integer, String> {
    private SecondActivity secondAct;
    private BookInfoActivity bookAct;
    private Context context;
    private Integer type;


    /* Set the values of the variables. */
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
        return HttpRequestHelper.downloadFromServer(type, context, params);
    }


    /* Get all book information from the json page and add it to an arraylist. */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        String title;
        String author;

        try {
            JSONObject bookStreamObj = new JSONObject(result);
            // If type is 1, the BookAsyncTask is called from the SecondActivity and if it is 2,
            // it is called from the BookInfoActivity. This way different information can be
            // retrieved.
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
                list.add("Title: " + title);
                list.add("Author: " + author);
                list.add("Publisher: " + publisher);
                list.add("Publication date: " + publishedDate);
                if (volumeObj.has("description")) {
                    String description = volumeObj.getString("description");
                    list.add(description);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Send arraylist with book information to either the SecondActivity or the
        // BookInfoActivity.
        if (type == 1) {
            this.secondAct.bookShow(list, idList);
        } else {
            this.bookAct.bookInfoShow(list);
        }
    }

}
