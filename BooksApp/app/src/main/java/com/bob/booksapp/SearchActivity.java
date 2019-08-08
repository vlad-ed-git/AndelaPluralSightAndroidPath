package com.bob.booksapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

      final EditText book_title_et =  findViewById(R.id.book_title_et);
      final EditText book_author_et =  findViewById(R.id.book_author_et);
      final EditText book_publisher_et =   findViewById(R.id.book_publisher_et);
      final EditText book_isbn_et =  findViewById(R.id.book_isbn_et);
      Button book_search_btn =  findViewById(R.id.book_search_btn);

        book_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_title = book_title_et.getText().toString().trim();
                String book_author = book_author_et.getText().toString().trim();
                String book_publisher = book_publisher_et.getText().toString().trim();
                String book_isbn = book_isbn_et.getText().toString().trim();

                if(!book_title.isEmpty() || !book_author.isEmpty() || !book_publisher.isEmpty() || !book_isbn.isEmpty()){
                    URL url_to_query =  ApiUtil.buildUrl(book_title,book_author,book_publisher,book_isbn);
                    startActivity((new Intent(SearchActivity.this, BooksListActivity.class)).putExtra("query", url_to_query));

                }
            }
        });

    }
}
