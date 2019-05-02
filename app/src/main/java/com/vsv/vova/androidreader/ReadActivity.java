package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.vsv.vova.androidreader.Room.Book;

import java.util.Calendar;


public class ReadActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    Intent intent;
    private static Uri uri = Uri.EMPTY;
    int pageNumber = 0;
    ReaderRepository repo = new ReaderRepository(this.getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        intent = getIntent();
        pdfView = findViewById(R.id.pdfView);
        pageNumber = intent.getIntExtra("page", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPdfView();
    }

    private void loadPdfView() {
        if (intent.hasExtra("uri")) {
            uri = Uri.parse(intent.getStringExtra("uri"));
        }
        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String title = uri.getLastPathSegment();
        try {
           title = title.replace("primary:Download/", "");
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        repo.insertAll(new Book(title,
                uri.toString(), pageNumber, Calendar.getInstance().getTimeInMillis()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", pageNumber);
        Log.d("mytag", pageNumber + " saveInst");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pageNumber = savedInstanceState.getInt("page");
        Log.d("mytag", pageNumber + " restoreInst");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        //Переменная которой присваивается страница при перемещении экрана
        pageNumber = page;
        Log.d("mytag", pageNumber + " page changed");
    }

    @Override
    public void loadComplete(int nbPages) {
    }

}


