package com.vsv.vova.androidreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.util.Map;
import java.util.Set;

public class ReadActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    int pageNumber;
    SharedPreferences sharedPreferences;
    private static Uri URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        Intent intent = getIntent();



        pdfView = (PDFView) findViewById(R.id.pdfView);
        Log.d("vvv", intent.getStringExtra("extra"));

        if(intent.getStringExtra("extra").equals("CONTINUE")){
            Log.d("vvv", intent.getStringExtra("extra")+"MyExtra");
            loadBook();
        } else {
            URI = Uri.parse(intent.getStringExtra("extra"));
        pdfView.fromUri(URI)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();}

    }


    @Override
    protected void onPause() {
        super.onPause();
        Integer i = pageNumber + 1;
        saveBook();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    private void saveBook (){
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uri", URI.toString());
        editor.putInt("page", pageNumber);
        editor.commit();
        Log.d("page", "Page Saved");
    }
    private int loadBook (){
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String loadUri = sharedPreferences.getString("uri", "");
        URI = Uri.parse(loadUri);
        int page = sharedPreferences.getInt("page",0 );
        pdfView.fromUri(URI)
                .defaultPage(page)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        Log.d("page", "Page Loaded");
        return page ;

    }



}
