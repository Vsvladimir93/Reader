package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;


import com.vsv.vova.androidreader.Realm.ReaderRealm;
import com.vsv.vova.androidreader.adapter.BookListAdapter;
import com.vsv.vova.androidreader.adapter.OnItemClickListener;
import com.vsv.vova.androidreader.adapter.RecyclerViewAdapter;
import com.vsv.vova.androidreader.model.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private final String LOG_TAG = "MainActivity";

    @BindView(R.id.buttonFind)
    Button btnFind;
    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;

    private static Uri uri;
    List<Book> bookList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnFind.setOnClickListener(this);

    }

    //Передать Uri книги

    // Вытащить название из списка
    // Найти по названию книгу в Реалме
    // Передать Uri

    @Override
    protected void onResume() {
        super.onResume();
        loadBookList();
    }

    private void openBook(Uri uri){
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("uri", uri.toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == RESULT_OK) {
            if (resultData != null) {
                uri = resultData.getData();
                openBook(uri);
            }
        }
    }

    private void loadBookList() {

        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookList = bookRealmQuery.findAll();

        bookList = ((RealmResults<Book>) bookList).sort("date" , Sort.DESCENDING);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(bookList);
        recyclerViewAdapter.setOnItemClickListener(this);

        booksRecyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(LOG_TAG, bookList.get(position).getTitle() + " ---------------------");
    }
}
