package com.bob.booksapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class BooksListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        final TextView book_query_response_tv = findViewById(R.id.book_query_response_tv);
        final ProgressBar loading_indicator  = findViewById(R.id.loading_indicator);

        //query the book api
        URL title_url = ApiUtil.buildUrl("android");
        FetchBooksAsync fetchBooksAsync = new FetchBooksAsync();
        fetchBooksAsync.result_delegator = new JsonStringsReceiver() {
            @Override
            public void delegate(String json_result) {
                loading_indicator.setVisibility(View.INVISIBLE);
                if(json_result != null){
                    book_query_response_tv.setText(json_result);
                }else{
                    book_query_response_tv.setText(R.string.err_txt);
                }
            }
        };
        fetchBooksAsync.execute(title_url);



    }
}
