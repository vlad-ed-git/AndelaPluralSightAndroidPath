package com.bob.booksapp;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;


public class FetchBooksAsync extends AsyncTask<URL, Void, String> {

    public JsonStringsReceiver result_delegator;

    @Override
    protected String doInBackground(URL... urls_to_query) {
        URL url_to_query = urls_to_query[0];
        try {
            return ApiUtil.getJSON(url_to_query);
        }catch (Exception e) {
            Log.d("FetchBooksAsync" , e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        result_delegator.delegate(result);
    }
}
