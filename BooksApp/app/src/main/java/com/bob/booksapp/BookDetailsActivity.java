package com.bob.booksapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.booksapp.databinding.ActivityBookDetailsBinding;

public class BookDetailsActivity extends AppCompatActivity {

        private ImageView book_cover_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Books book = getIntent().getParcelableExtra("Book");

        book_cover_iv = findViewById(R.id.book_cover_iv);
        // auto created when we created the data binding stuff
        ActivityBookDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_book_details);
        binding.setBook(book);
    }
}
