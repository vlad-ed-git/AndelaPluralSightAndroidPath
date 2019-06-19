package com.bob.booksapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {

    private static final String QUERY_PARAM_KEY = "q";

    private ApiUtil(){}

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes/";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyCcFM2mwD7i0OHFOoPLZN-6TB4uGuOeUSQ";

    public static URL buildUrl(String title_to_query){
        Uri uri_to_query = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_KEY, title_to_query)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        URL url_to_query = null;
        try{
            url_to_query = new URL(uri_to_query.toString());
        }catch (Exception e){
            Log.d("Error building query: " , e.getMessage());
        }
        return url_to_query;
    }

    public static String getJSON(URL url_to_query) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url_to_query.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        // scanner buffers the data and encodes the character to utf-16, the android format
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        try {
            boolean hasNextData = scanner.hasNext();
            if (hasNextData)
                return scanner.next();
            else
                return null;

        }catch (Exception e){
            Log.d("Exc scanning query:", e.getMessage());
            return null;
        }finally{
            // close the connection
            httpURLConnection.disconnect();
        }

    }

    public static ArrayList<Books> getBooksFromJson(String jsonString){

        final String ID = "id" ;
        final String TITLE = "title";
        final String SUBTITLE ="subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE ="publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";

        try{
            JSONObject resultsObject = new JSONObject(jsonString);
            JSONArray booksArray =  resultsObject.getJSONArray(ITEMS);

            ArrayList<Books>  booksArrayList = new ArrayList<>();

            int number_of_books = booksArray.length();

            for (int i = 0; i < number_of_books; i++){

                JSONObject  bookJson = booksArray.getJSONObject(i);
                JSONObject  bookVolumeInfo = bookJson.getJSONObject(VOLUME_INFO);
                int total_authors = bookVolumeInfo.getJSONArray(AUTHORS).length();
                String[] authors = new String[total_authors];
                for(int j=0; j<total_authors; j++){
                   authors[j] = bookVolumeInfo.getJSONArray(AUTHORS).get(j).toString();
                }

                Books book = new Books(bookJson.getString(ID), bookVolumeInfo.getString(TITLE), (bookVolumeInfo.isNull(SUBTITLE) ? "-subtitle not found-" : bookVolumeInfo.getString(SUBTITLE)), authors, bookVolumeInfo.getString(PUBLISHER), bookVolumeInfo.getString(PUBLISHED_DATE) );
                booksArrayList.add(book);
            }
            return booksArrayList;

        }catch (Exception e){
            Log.d("parsing Json books" , e.getMessage());
            return null;
        }
    }
}
