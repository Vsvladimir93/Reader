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
import com.vsv.vova.androidreader.model.Book;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmQuery;


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
        //Если была нажата кнопка Continue ( - extra == CONTINUE)
        //Загрузка по Uri из БД
        if(intent.getStringExtra("extra").equals("CONTINUE")){
            Log.d("vvv","loadPdfView");
            loadBook();
        } else {
            //Если окно открылось через кнопку Find (onActivityResult)
            //Загрузка по переданному Uri
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("vvv", "onPause methode");
        saveBook();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        //Переменная которой присваивается страница при перемещении экрана
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {

    }
          //Метод сохранения книги
    private void saveBook() {

          //Достаем название книги из Uri
          File file = new File(uri.getPath());
          final String title =  file.getName();
          //Запись в Реалм
    ReaderRealm.getRealm().executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            ///Запрос колличеста объектов в Реалме для подсчета и добавления Id для следующего
            RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
            bookRealmQuery.findAll();
            int count = (int) bookRealmQuery.count();
            Log.d("vvv", "get count of objects in realm - " + count);
            ///

            //Достаем название

             //Если в Реалме есть книга с таким названием
             bookRealmQuery.equalTo("title", title);
             Book book;
             if((book = bookRealmQuery.findFirst()) != null ){
                 //то - меняется только номер страницы
                 book.setPage(pageNumber);
                 realm.copyToRealmOrUpdate(book);
             }else {
                 //Иначе записывается книга с номером стр. и названием
                 book.setPage(pageNumber);
                 book.setTitle(title);
                 realm.copyToRealmOrUpdate(book);
             }
            Log.d("vvv","book saved - title - " + title +
                    ": Id - " + count + ": PageNumber - " + pageNumber);

        }
    });
        //Сохраняется строка с названием последней открытой книги
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
                ed.putString("last_book", title);
                ed.commit();
                Log.d("vvv","SharedPref Commit - title of last book - " + title);
    }

    //Метод который загружает книгу из БД и запускает PDFView с ней
    private void loadBook (){
        //Загружается название последней открытой книги
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        String title = sh.getString("last_book","");
        Log.d("vvv", "SharedPref get title of last Book - " + title);
        //По названию загружается книга из БД
        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookRealmQuery.equalTo("title", title);
        Book book = bookRealmQuery.findFirst();

        uri = book.getUri();
        pdfView.fromUri(uri)
                .defaultPage(book.getPage())
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
}
