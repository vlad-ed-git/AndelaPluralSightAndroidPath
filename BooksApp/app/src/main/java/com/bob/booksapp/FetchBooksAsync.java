package com.bob.booksapp;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;


public class FetchBooksAsync extends AsyncTask<URL, Void, ArrayList<Books>> {

    public JsonStringsReceiver result_delegator;

    @Override
    protected ArrayList<Books> doInBackground(URL... urls_to_query) {
        URL url_to_query = urls_to_query[0];
        try {

            return ApiUtil.getBooksFromJson(ApiUtil.getJSON(url_to_query));
        }catch (Exception e) {
            Log.d("FetchBooksAsync" , e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Books> booksArrayList) {
        super.onPostExecute(booksArrayList);

        result_delegator.delegate(booksArrayList);
    }
}
