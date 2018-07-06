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
    Intent intent;
    private static Uri uri;
    int pageNumber;
    long maximumId;
    int countObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        RealmQuery<Book> bookQuery = ReaderRealm.getRealm().where(Book.class);
        countObjects = (int) bookQuery.count();
        Log.d("vvv", "get count of objects in realm - " + countObjects);
        if(bookQuery.max("id")==null){
            maximumId = 0;
        }else{
            maximumId = (long) bookQuery.max("id");
        }
        Log.d("vvv", "maximum Id - " + maximumId);

        intent = getIntent();
        pdfView = findViewById(R.id.pdfView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPdfView();
    }

    private void loadPdfView() {
        //Если была нажата кнопка Continue ( - extra == CONTINUE)
        //Загрузка по Uri из БД
        if (intent.getStringExtra("extra").equals("CONTINUE")) {
            Log.d("vvv", "loadPdfView");
            loadBook();
        } else {
            if(intent.getStringExtra("uri")!=null){
            uri = uri.parse(intent.getStringExtra("uri"));
            } else{
            //Если окно открылось через кнопку Find (onActivityResult)
            //Загрузка по переданному Uri
            uri = uri.parse(intent.getStringExtra("extra"));
            }
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
    protected void onPause() {
        super.onPause();
        Log.d("vvv", "onPause method");
        saveBook();
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

    //Метод сохранения книги
    private void saveBook() {
        //Достаем название книги из Uri
        File file = new File(uri.getPath());
        final String bookTitle = file.getName();

        //Запись в Реалм
        ReaderRealm.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //Достаем название
                //Если в Реалме есть книга с таким названием
                RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
                bookRealmQuery.equalTo("title", bookTitle);
                Book book;
                if ((book = bookRealmQuery.findFirst()) != null) {
                   /* Log.d("vvv", "saveBook - find same book - Update.");
                    book.setId(maximumId +1);
                    book.setPage(pageNumber);*/
                   //Перезаписывается книга с новым ID и страницей
                    book.deleteFromRealm();
                    Book newBook = new Book(bookTitle, uri, pageNumber, maximumId+1);
                    realm.copyToRealmOrUpdate(newBook);
                } else {
                    //Записывается новая книга
                    Log.d("vvv", "saveBook - dont find the same book - Write");
                    Book newBook = new Book(bookTitle, uri, pageNumber, maximumId +1);
                    realm.copyToRealm(newBook);
                }
                Log.d("vvv", "book saved.title- " + bookTitle + " Id- " + (maximumId +1) + " Page- " + pageNumber);
            }
        });

       /* //Сохраняется строка с названием последней открытой книги
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.putString("last_book", bookTitle);
        ed.putInt("last_book_page", pageNumber);
        ed.apply();
        Log.d("vvv", "SharedPref Commit - title of last book - " + bookTitle);*/
    }

    //Метод который загружает книгу из БД и запускает PDFView
    private void loadBook() {
        /*//Загружается название последней открытой книги
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        String bookTitle = sh.getString("last_book", "");*/
        //По названию загружается книга из БД
        //todo Вынести в отдельный метод - ReaderRealm --
        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookRealmQuery.equalTo("id", maximumId);
        Book book = bookRealmQuery.findFirst();

        uri = book.getUri();
        pdfView.fromUri(uri)
                .defaultPage((int)book.getPage())
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

   /* private void realmSort(){
        Book book;
        if(maximumId>=10){
            RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
            bookRealmQuery.equalTo("id",1);
            bookRealmQuery.findFirst().deleteFromRealm();

            for(int i = 2; i<=10; i++){
                RealmQuery<Book> bookRealmQuery1 = ReaderRealm.getRealm().where(Book.class);
                bookRealmQuery1.equalTo("id",i);
                Uri uri = bookRealmQuery1.findFirst().getUri();
                int page = (int)bookRealmQuery1.findFirst().getPage();
                String title = bookRealmQuery1.findFirst().getTitle();
                bookRealmQuery1.findFirst().deleteFromRealm();
                book = new Book(title, uri, page, i-1);


            }
        }




    }*/

}


