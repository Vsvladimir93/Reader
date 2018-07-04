package com.vsv.vova.androidreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.vsv.vova.androidreader.model.Book;

import java.util.List;

public class BookListAdapter implements ListAdapter {

    List<Book> bookList;
    LayoutInflater layoutInflater;

    public BookListAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = layoutInflater.inflate(R.layout.book_item_layout, parent,false );
        }
    Book book = getBook(position);

    TextView textView = (TextView) view.findViewById(R.id.textView);
    TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        Integer page = (int) book.getPage() + 1;
        textView2.setText(page.toString());
        textView.setText(book.getTitle());

        return view;
    }

    public Book getBook(int position){
        return bookList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return bookList.isEmpty();
    }
}
