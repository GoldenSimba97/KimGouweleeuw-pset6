package com.example.kimgo.kimgouweleeuw_pset6;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kimgo on 10-10-2017.
 */

public class HttpRequestHelper {

    static synchronized String downloadFromServer(int type, String... params) {
        String result = "";
        String chosenTag = params[0];

        URL url = null;
        try {
            if (type == 1) {
                url = new URL("https://www.googleapis.com/books/v1/volumes?" + "&q=" + chosenTag + "&key=" + "AIzaSyBWNT3DLUBrE1-93sJurjOewBQ91ZRmf6g");
            } else {
                url = new URL("https://www.googleapis.com/books/v1/volumes/" + chosenTag + "?key=" + "AIzaSyBWNT3DLUBrE1-93sJurjOewBQ91ZRmf6g");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connect;

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
            }
        }

        return result;
    }

}
