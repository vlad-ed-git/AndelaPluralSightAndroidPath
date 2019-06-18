package com.bob.booksapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
