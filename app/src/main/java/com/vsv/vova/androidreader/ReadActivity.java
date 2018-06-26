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

import java.io.File;

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

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveBook();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        //Переменная которая хранит страницу текущую
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {

    }
        //Метод сохранения книги
    private void saveBook() {
        //Запись в Реалм
    ReaderRealm.getRealm().executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            ///Запрос колличеста объектов в Реалме для подсчета и добавления Id для следующего
            RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
            bookRealmQuery.findAll();
            int id = (int) bookRealmQuery.count();
            Log.d("vvv", "get count of objects in realm - " + id);
            ///
            ///Сохранение объекта в Реалм
             //Достаем название
             String title = "titleErr";
             if(uri != null){ File file = new File(uri.getPath());
               title =  file.getName();
             }
             //Если в Реалме есть книга с таким названием
             bookRealmQuery.equalTo("title", title);
             //Найти первую, сравнить с текущей, перезаписать id и страницу
             //Если нету, то записать новый экземпляр книги с последними данными.


            /*Ошибка 26,06,18
               Caused by: io.realm.exceptions.RealmException: Primary key field 'id' cannot be changed after object was created.
                      at io.realm.com_vsv_vova_androidreader_BookRealmProxy.realmSet$id(com_vsv_vova_androidreader_BookRealmProxy.java:111)
                      at com.vsv.vova.androidreader.Book.setId(Book.java:61)
                      at com.vsv.vova.androidreader.ReadActivity$1.execute(ReadActivity.java:105)
                      at io.realm.Realm.executeTransaction(Realm.java:1405)
                      at com.vsv.vova.androidreader.ReadActivity.saveBook(ReadActivity.java:85)
                      at com.vsv.vova.androidreader.ReadActivity.onPause(ReadActivity.java:68)
            */

            Book book = new Book();
                book.setPage(pageNumber);
                book.setTitle(title);
                book.setId(id+1);
            realm.copyToRealmOrUpdate(book);

            Log.d("vvv","book saved - title - " + title +
                    ": Id - " + (id + 1) + ": PageNumber - " + pageNumber);

        }
    });

    }

    private void loadBook (){
        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookRealmQuery.findAll();
        ///Подсчет объектов в Реалме
        int counterId = (int) bookRealmQuery.count();
        Log.d("vvv", "Id counter  = " + counterId);
        ///Достает последний объект по Id
        bookRealmQuery.equalTo("id", counterId);
        Book book = bookRealmQuery.findFirst();
        Integer i = book.getPage();
        uri = book.getUri();
        Log.d("vvv",i.toString());
        try{
        pdfView.fromUri(uri)
                .defaultPage(book.getPage())
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        }catch(Exception e ){System.out.println("EL ERRORE");}

    }



}
