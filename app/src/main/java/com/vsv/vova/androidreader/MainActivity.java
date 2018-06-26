package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnFind;
    Button btnContinue;
    public static final int REQUEST_CODE_FIND = 1;
    private static Uri uri;

/*    ListView listView;
    List<Book> bookList;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFind = (Button) findViewById(R.id.buttonFind);
        btnFind.setOnClickListener(this);
        btnContinue = (Button) findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(this);

        RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookRealmQuery.findAll();
        int x = (int) bookRealmQuery.count();
        Log.d("vvv", "Number of books in Realm = "  + x);

      /*  listView = (ListView) findViewById(R.id.listView);
        bookList = new ArrayList<>();
        Book book1 = new Book();
        book1.setPage(9);
        book1.setTitle("title1");
        Book book2 = new Book();
        book2.setPage(10);
        book2.setTitle("title2");
        bookList.add(book1);
        bookList.add(book2);*/

       /* RealmQuery<Book> bookRealmQuery = ReaderRealm.getRealm().where(Book.class);
        bookList.addAll(bookRealmQuery.findAll());
*/


//        ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, bookArrayList);

      /*  BookListAdapter bookListAdapter = new BookListAdapter(bookList, this);
        listView.setAdapter(bookListAdapter);
*/

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

}
