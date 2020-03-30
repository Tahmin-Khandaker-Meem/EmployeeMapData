package com.example.employeemapdata;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask {
    String data="";
    URL url;
    HttpURLConnection httpsURLConnection;
    InputStream inputStream;
    BufferedReader br;
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        MainActivity.textViewData = data;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            data = "";
            url = new URL("https://anontech.info/courses/cse491/employees.json");
            httpsURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpsURLConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = br.readLine()) != null) {

                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        // Perfect
    }
}
