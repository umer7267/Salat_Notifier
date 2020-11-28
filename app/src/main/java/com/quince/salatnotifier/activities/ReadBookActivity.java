package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.quince.salatnotifier.MainActivity;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.databinding.ActivityReadBookBinding;
import com.quince.salatnotifier.utility.DefaultLinkHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.github.barteksc.pdfviewer.PDFView;

public class ReadBookActivity extends AppCompatActivity {
    private static final String TAG = "ReadBookActivity";
    private Context context = ReadBookActivity.this;

    private ActivityReadBookBinding binding;
    String URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        URI = intent.getStringExtra("URI");

        Log.d(TAG, "onCreate: " + URI);

        new ConvertURL().execute();
    }

    public class ConvertURL extends AsyncTask<Void, Void, InputStream> {

        @Override
        protected InputStream doInBackground(Void... voids) {
            InputStream inputStream = null;
            try
            {
                URL url = new URL(URI);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e)
            {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            loadFromURI(inputStream);
            super.onPostExecute(inputStream);
        }
    }

    private void loadFromURI(InputStream input) {
        Log.d(TAG, "loadFromURI: " + input);
        binding.pdfView.fromStream(input)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad(nbPages -> binding.loadingBook.setVisibility(View.GONE))
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(8)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .linkHandler(new DefaultLinkHandler(binding.pdfView, context))
                .pageFitPolicy(FitPolicy.BOTH) // mode to fit pages in the view
                .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(true) // make a fling change only a single page like ViewPager
                .nightMode(false) //
                .load();
    }
}