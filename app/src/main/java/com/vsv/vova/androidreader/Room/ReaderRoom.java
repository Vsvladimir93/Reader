package com.vsv.vova.androidreader.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class}, version = 1)
public abstract class ReaderRoom extends RoomDatabase {

    public abstract BookDao bookDao();

    private static volatile ReaderRoom INSTANCE;

    public static ReaderRoom getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (ReaderRoom.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReaderRoom.class,"book_database")
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
