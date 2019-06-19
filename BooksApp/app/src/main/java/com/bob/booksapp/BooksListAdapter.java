package com.bob.booksapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksViewHolder> {

    private ArrayList<Books> booksArrayList;

    public BooksListAdapter(ArrayList<Books> booksArrayList){
            this.booksArrayList = booksArrayList;
    }


    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_item, viewGroup, false);
        return new BooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder booksViewHolder, int position) {
        Books book = booksArrayList.get(position);
        booksViewHolder.bindData(book);
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {

        ImageView book_open_icon;
        TextView book_title,book_authors,book_published_date, book_publisher;
        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            book_open_icon = itemView.findViewById(R.id.book_open_icon);
            book_title = itemView.findViewById(R.id.book_title);
            book_authors = itemView.findViewById(R.id.book_authors);
            book_publisher = itemView.findViewById(R.id.book_publisher);
            book_published_date = itemView.findViewById(R.id.book_published_date);
        }

        public void bindData(Books book){
            book_title.setText(book.title);
            StringBuilder authors = new StringBuilder();
            int total_authors = book.authors.length;
            for (String author: book.authors) {
                total_authors --;
                authors.append(author);
                if(total_authors > 0 )
                    authors.append(", ");

            }
            book_authors.setText(authors.toString());
            book_publisher.setText(book.publisher);
            book_published_date.setText(book.publishedDate);

        }

    }

}
