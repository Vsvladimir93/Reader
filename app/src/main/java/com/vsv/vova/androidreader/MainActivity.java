package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.vsv.vova.androidreader.Room.Book;
import com.vsv.vova.androidreader.adapter.OnItemClickListener;
import com.vsv.vova.androidreader.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private final String LOG_TAG = "MainActivity";

    @BindView(R.id.buttonFind)
    Button btnFind;
    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;

    private static Uri uri;
    List<Book> bookList = new ArrayList<>();

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

    private void openBook(Uri uri, int page){
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("uri", uri.toString());
        intent.putExtra("page", page);
        startActivity(intent);
    }

    private void loadBookList() {
        ReaderRepository repo = new ReaderRepository(this.getApplication());
        if(repo.getAll() != null){
            bookList = repo.getAll();
        }
        recyclerInit(bookList);
    }

    private void recyclerInit(List<Book> bookList){
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(bookList);
        recyclerViewAdapter.setOnItemClickListener(this);
        booksRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
       openBook(Uri.parse(bookList.get(position).getUri()) ,bookList.get(position).getPage());
    }
}
