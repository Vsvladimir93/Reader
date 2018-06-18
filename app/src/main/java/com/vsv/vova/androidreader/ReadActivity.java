package com.vsv.vova.androidreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ReadActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    int pageNumber;
    private static Uri uri;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        intent = getIntent();
        pdfView = findViewById(R.id.pdfView);
        loadPdfView();

    }

    private void loadPdfView(){
        if(intent.getStringExtra("extra").equals("CONTINUE")){
            Log.d("vvv","loadPdfView");
            loadBook();
        } else {
            uri = uri.parse(intent.getStringExtra("extra"));
            pdfView.fromUri(uri)
                    .defaultPage(pageNumber)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d(ReadActivity.class.getSimpleName(),
                    "onResume: " + ReaderRealm.getRealm().where(Book.class).isNotNull("title").findAll().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveBook();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    private void saveBook() {
    ReaderRealm.getRealm().executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            realm.copyToRealmOrUpdate(new Book("name1",uri, 1, pageNumber));
            Log.d("vvv","book saved");
        }
    });

    }

    private void loadBook (){
        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookRealmQuery.equalTo("id", 1);
        Book book = bookRealmQuery.findFirst();
        Integer i = book.getPage();
        uri = book.getUri();
        Log.d("vvv",i.toString());
        pdfView.fromUri(uri)
                .defaultPage(book.getPage())
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }



}
