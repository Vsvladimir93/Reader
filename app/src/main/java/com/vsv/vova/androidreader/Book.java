package com.vsv.vova.androidreader;

import android.net.Uri;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

public class Book extends RealmObject {

    public Book() {
    }

    public Book(String title, Uri uri, int id) {
        this.title = title;
        this.uri = uri.toString();
        this.id = id;
    }


    @PrimaryKey
    private int id;
    private String title;
    private String uri;

    public int getId() {
        return id;
    }

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

    public void setId(int id) {
        this.id = id;
    }
}
