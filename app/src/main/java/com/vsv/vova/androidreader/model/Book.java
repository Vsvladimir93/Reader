package com.vsv.vova.androidreader.model;

import android.net.Uri;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

public class Book extends RealmObject {

    public Book() {
    }

    public Book(String title, Uri uri, long page, long date) {
        this.title = title;
        this.uri = uri.toString();
        this.page = page;
        this.date = date;
    }

    @PrimaryKey
    private String title;
    private long date;
    private String uri;
    private long page;

    public long getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) { this.date = date; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

}
