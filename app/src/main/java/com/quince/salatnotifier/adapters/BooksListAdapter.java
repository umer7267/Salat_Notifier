package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.ReadBookActivity;
import com.quince.salatnotifier.model.BookModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BookViewHolder> {

    private Context context;
    private List<BookModel> booksList;

    public BooksListAdapter(Context context, List<BookModel> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_book_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookModel book = booksList.get(position);

        holder.book_no.setText(String.valueOf(position+1));
        holder.book_name.setText(book.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String URL = ConstantsUtilities.BOOK_BASE_URL + book.getBook();

                Intent intent = new Intent(context, ReadBookActivity.class);
                intent.putExtra("URI", URL);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView book_no, book_name;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            book_no = itemView.findViewById(R.id.book_no);
            book_name = itemView.findViewById(R.id.book_name);

        }
    }
}
