package com.vsv.vova.androidreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vsv.vova.androidreader.model.Book;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnFind;
    Button btnContinue;
    public static final int REQUEST_CODE_FIND = 1;
    private static Uri uri;

    ListView listView;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFind = findViewById(R.id.buttonFind);
        btnFind.setOnClickListener(this);
        btnContinue = findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(this);
        listView = findViewById(R.id.listView);

        loadList();

        final Context context = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.d("vvv", "position - " + position + " id - " + id + "");
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("vvv", "item selected " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();





    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonFind:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQUEST_CODE_FIND);
                Log.d("vvv", "Request code find");
                break;
            case R.id.buttonContinue:
                intent = new Intent(this, ReadActivity.class);
                intent.putExtra("extra", "CONTINUE");
                startActivity(intent);
                Log.d("vvv", "continue");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == REQUEST_CODE_FIND && resultCode == RESULT_OK) {
            Log.d("vvv", "onResult Find");
            uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Intent intent = new Intent(this, ReadActivity.class);
                intent.putExtra("extra", uri.toString());
                startActivity(intent);
            }

        }

    }

    private void loadList(){
        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        // временно --
//        long maxId = (long) bookRealmQuery.max("id");
//        bookRealmQuery.between("id", (maxId - 10),maxId);
        bookList = bookRealmQuery.findAll();
//        int numObj = (int) bookRealmQuery.count();


        BookListAdapter bookListAdapter = new BookListAdapter(bookList, this);
        listView.setAdapter(bookListAdapter);

    }

}
