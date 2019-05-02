package com.vsv.vova.androidreader.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book ORDER BY date DESC")
    List<Book> getAll();

    @Query("SELECT * FROM book WHERE title LIKE :title LIMIT 1")
    Book findByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book ... books);

    @Update
    void updateBook(Book book);

    @Delete
    void deleteBook(Book book);
}
