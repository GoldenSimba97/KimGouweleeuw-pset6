package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.os.AsyncTask;
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

    BookAsyncTask(SecondActivity second) {
        this.secondAct = second;
        this.context = this.secondAct.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Searching...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<String> list = new ArrayList<>();

        try {
            JSONObject trackStreamObj = new JSONObject(result);
            JSONArray resultObj = trackStreamObj.getJSONArray("items");
//            JSONObject trackMatchesObj = resultObj.getJSONObject("trackmatches");
//            JSONArray trackObj = trackMatchesObj.getJSONArray("track");
            for (int i = 0; i < resultObj.length(); ++i) {
                JSONObject book = resultObj.getJSONObject(i);
                JSONObject volumeObj = book.getJSONObject("volumeInfo");
                String title = volumeObj.getString("title");
                JSONArray authorObj = volumeObj.getJSONArray("authors");
                String author = authorObj.getString(0);
                list.add(title + " - " + author);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.secondAct.bookStartIntent(list);
    }

}
