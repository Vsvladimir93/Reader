package com.vsv.vova.androidreader;

import android.app.Application;
import android.os.AsyncTask;

import com.vsv.vova.androidreader.Room.Book;
import com.vsv.vova.androidreader.Room.BookDao;
import com.vsv.vova.androidreader.Room.ReaderRoom;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReaderRepository {

    private BookDao bookDao;

    public ReaderRepository(Application application){
        ReaderRoom db = ReaderRoom.getDatabase(application);
        bookDao = db.bookDao();
    }

    /**
     * get all books from db in descending order by Date
     * @return List<Book> or null
     */
    public List<Book> getAll(){
        try {
            return new getAllAsyncTask(bookDao).execute().get();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return null;
        } catch (ExecutionException ee) {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * get book by title from db
     * @param title - Book title
     * @return Book or null
     */
    public Book findByTitle(String title){
        try {
            return new findByTitleAsyncTask(bookDao).execute(title).get();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return null;
        } catch (ExecutionException ee) {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * write books in db
     * @param books
     */
    public void insertAll(Book... books){
        new insertAsyncTask(bookDao).execute(books[0]);
    }

    private static class insertAsyncTask extends AsyncTask<Book, Void, Void>{

        private BookDao mAsyncBookDao;

        insertAsyncTask(BookDao dao){
            mAsyncBookDao = dao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            mAsyncBookDao.insertAll(books[0]);
            return null;
        }
    }

    private static class findByTitleAsyncTask extends AsyncTask<String, Void, Book>{

        private BookDao mAsyncBookDao;

        findByTitleAsyncTask(BookDao dao){
            mAsyncBookDao = dao;
        }

        @Override
        protected Book doInBackground(String... strings) {
            return mAsyncBookDao.findByTitle(strings[0]);
        }
    }

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<Book>>{

        private BookDao mAsyncBookDao;

        getAllAsyncTask(BookDao dao){
            mAsyncBookDao = dao;
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            return mAsyncBookDao.getAll();
        }
    }
}
