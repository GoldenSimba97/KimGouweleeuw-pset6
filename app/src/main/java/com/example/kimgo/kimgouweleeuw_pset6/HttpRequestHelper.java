/*
 * HttpRequestHelper class. In this class the Json file is
 * retrieved with the url from the search query. This Json
 * file is then converted to a string.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequestHelper {

    static synchronized String downloadFromServer(int type, Context context, String... params) {
        String result = "";
        String chosenTag = params[0];

        // Sets the url depending on whether the request came from the SecondActivity or the
        // BookInfoActivity.
        URL url = null;
        try {
            if (type == 1) {
                url = new URL("https://www.googleapis.com/books/v1/volumes?" + "&q=" + chosenTag +
                        "&key=" + "AIzaSyBWNT3DLUBrE1-93sJurjOewBQ91ZRmf6g");
            } else {
                url = new URL("https://www.googleapis.com/books/v1/volumes/" + chosenTag + "?key=" +
                        "AIzaSyBWNT3DLUBrE1-93sJurjOewBQ91ZRmf6g");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        HttpURLConnection connect;

        // Gets the json file from the url.
        if (url != null) {
            try {
                connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("GET");

                Integer responseCode = connect.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    BufferedReader bufferedReader = new BufferedReader
                            (new InputStreamReader(connect.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

}
