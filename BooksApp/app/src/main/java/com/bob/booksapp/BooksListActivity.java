package com.bob.booksapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class BooksListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView book_query_response_rv;
    private ProgressBar loading_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        book_query_response_rv = findViewById(R.id.book_query_response_rv);
        loading_indicator  = findViewById(R.id.loading_indicator);
        loading_indicator.setVisibility(View.INVISIBLE);
    }

    //query the book api
    private void loadBooks(URL title_url){
        loading_indicator.setVisibility(View.VISIBLE);
        FetchBooksAsync fetchBooksAsync = new FetchBooksAsync();
        fetchBooksAsync.result_delegator = new JsonStringsReceiver() {
            @Override
            public void delegate(ArrayList<Books> booksArrayList) {
                loading_indicator.setVisibility(View.INVISIBLE);
                if(booksArrayList != null){
                    LinearLayoutManager booksListLayoutManager = new LinearLayoutManager(BooksListActivity.this, LinearLayoutManager.VERTICAL, false);
                    book_query_response_rv.setLayoutManager(booksListLayoutManager);
                    BooksListAdapter adapter = new BooksListAdapter(booksArrayList);
                    book_query_response_rv.setAdapter(adapter);
                }else{
                    Toast.makeText(BooksListActivity.this, R.string.err_txt, Toast.LENGTH_LONG).show();
                }
            }
        };
        fetchBooksAsync.execute(title_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.books_list_menu, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchActionView = (SearchView) searchMenuItem.getActionView();
        searchActionView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            URL title_url = ApiUtil.buildUrl(query);
            loadBooks(title_url);
        }catch (Exception e){
            Log.d("On search query submit", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
