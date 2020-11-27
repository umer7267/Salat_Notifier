package com.quince.salatnotifier.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;

public class DefaultLinkHandler implements LinkHandler {

    private static final String TAG = DefaultLinkHandler.class.getSimpleName();

    private PDFView pdfView;
    private Context context;

    public DefaultLinkHandler(PDFView pdfView, Context context) {
        this.pdfView = pdfView;
        this.context = context;
    }

    @Override
    public void handleLinkEvent(LinkTapEvent event) {
        String uri = event.getLink().getUri();
        Integer page = event.getLink().getDestPageIdx();
        if (uri != null && !uri.isEmpty()) {
            handleUri(uri);
        } else if (page != null) {
            handlePage(page);
        }
    }

    private void handleUri(String uri) {

            Uri parsedUri = Uri.parse(uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, parsedUri);
            Context context = pdfView.getContext();
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Log.w(TAG, "No activity found for URI: " + uri);
            }
    }

    private void handlePage(int page) {
        pdfView.jumpTo(page);
    }
}
