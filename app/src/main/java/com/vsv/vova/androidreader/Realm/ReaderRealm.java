package com.vsv.vova.androidreader.Realm;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.vsv.vova.androidreader.model.Book;

import java.io.File;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmQuery;

public class ReaderRealm {

    private static final String LOG_TAG = "ReaderRealm";

    private static ReaderRealm readerRealm = new ReaderRealm();
    private Realm realm;

    private ReaderRealm() {}

    public static ReaderRealm getInstance() {
        return readerRealm;
    }

    public static void init(Context context) {
        Realm.init(context);
        getInstance().realm = Realm.getDefaultInstance();
    }

    public static Realm getRealm() {
        return getInstance().realm;
    }

    public static void saveBookRdb(final Uri uri, final int page){

        //Достаем название из Uri пути
        File file = new File(uri.getPath());
        final String bookTitle = file.getName();

        ReaderRealm.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //Если в Реалме есть книга с таким названием то -
                RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
                bookRealmQuery.equalTo("title", bookTitle);
                Book book;
                if ((book = bookRealmQuery.findFirst()) != null) {
                    //Перезаписывается книга с новой датой и страницей
                    book.setDate(Calendar.getInstance().getTimeInMillis());
                    book.setPage(page);
                } else {
                    //Иначе записывается новая книга
                    Book newBook = new Book(bookTitle, uri, page, Calendar.getInstance().getTimeInMillis());
                    realm.copyToRealm(newBook);
                }
            }
        });

    }
}
