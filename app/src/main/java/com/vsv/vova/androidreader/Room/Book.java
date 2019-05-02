package com.vsv.vova.androidreader.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book {

    public Book(String title, String uri, int page, long date){
        this.title = title;
        this.uri = uri;
        this.page = page;
        this.date = date;
    }

    @NonNull
    @PrimaryKey
    String title;

    @ColumnInfo(name = "uri")
    String uri;

    @ColumnInfo(name = "page")
    int page;

    @ColumnInfo(name = "date")
    long date;

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public int getPage() {
        return page;
    }

    public long getDate() {
        return date;
    }
}
