package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.adapters.BooksListAdapter;
import com.quince.salatnotifier.databinding.ActivityBooksListBinding;
import com.quince.salatnotifier.model.BookModel;
import com.quince.salatnotifier.model.HadeesModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity {
    private static final String TAG = "BooksListActivity";
    private Context context = BooksListActivity.this;

    private ActivityBooksListBinding binding;

    private List<BookModel> booksList;
    private BooksListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBooksListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        booksList = new ArrayList<>();
        adapter = new BooksListAdapter(context, booksList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.booksLists.setLayoutManager(linearLayoutManager);
        binding.booksLists.setAdapter(adapter);

        getBooksList();
    }

    private void getBooksList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Books...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantsUtilities.API_BASE_URL + "books", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("status")){

                    JSONArray jsonArray = jsonObject.getJSONArray("books");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject hadees = jsonArray.getJSONObject(i);

                        BookModel book = new BookModel();

                        book.setName(hadees.getString("name"));
                        book.setBook(hadees.getString("book"));

                        booksList.add(book);

                    }

                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Unexpected Error!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e){
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}