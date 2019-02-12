package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.vsv.vova.androidreader.Realm.ReaderRealm;


public class ReadActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    Intent intent;
    private static Uri uri;
    int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        intent = getIntent();
        pdfView = findViewById(R.id.pdfView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPdfView();
    }

    private void loadPdfView() {
            if(intent.getStringExtra("uri")!=null){
            uri = Uri.parse(intent.getStringExtra("uri"));
            }
            pageNumber = (int) intent.getLongExtra("page", 0L);
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
        Log.d("vvv", "onPause method");
        ReaderRealm.saveBookRdb(uri, pageNumber);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page",pageNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pageNumber = savedInstanceState.getInt("page");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        //Переменная которой присваивается страница при перемещении экрана
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
    }

}


