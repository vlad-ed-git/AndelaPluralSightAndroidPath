package com.bob.booksapp;

import android.content.Intent;
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

    public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView book_open_icon;
        TextView book_title,book_authors,book_published_date, book_publisher;
        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            book_open_icon = itemView.findViewById(R.id.book_open_icon);
            book_title = itemView.findViewById(R.id.book_title);
            book_authors = itemView.findViewById(R.id.book_authors);
            book_publisher = itemView.findViewById(R.id.book_publisher);
            book_published_date = itemView.findViewById(R.id.book_published_date_tv);
            itemView.setOnClickListener(this);
        }

        public void bindData(Books book){
            book_title.setText(book.title);
            book_authors.setText(book.authors);
            book_publisher.setText(book.publisher);
            book_published_date.setText(book.publishedDate);

        }

        @Override
        public void onClick(View v) {
            int clicked_item_position = getAdapterPosition();
            Books book = booksArrayList.get(clicked_item_position);
            v.getContext().startActivity((new Intent(v.getContext(), BookDetailsActivity.class)).putExtra("Book", book));
        }
    }

}
