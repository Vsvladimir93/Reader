package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.vsv.vova.androidreader.Realm.ReaderRealm;
import com.vsv.vova.androidreader.adapter.OnItemClickListener;
import com.vsv.vova.androidreader.adapter.RecyclerViewAdapter;
import com.vsv.vova.androidreader.model.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookList();
    }

    private void openBook(Uri uri, long page){
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("uri", uri.toString());
        intent.putExtra("page", page);
        startActivity(intent);
    }

    @OnClick(R.id.buttonFind)
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
                openBook(uri, 0);
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
       openBook(bookList.get(position).getUri() ,bookList.get(position).getPage());
    }
}
