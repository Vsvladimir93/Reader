package com.vsv.vova.androidreader.adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsv.vova.androidreader.R;
import com.vsv.vova.androidreader.Room.Book;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {


    private OnItemClickListener onItemClickListener;
    private List<Book> bookList;

    public RecyclerViewAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_item, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
        recyclerViewHolder.titleTxV.setText(bookList.get(i).getTitle());
        recyclerViewHolder.pageTxV.setText(String.format("%d", bookList.get(i).getPage() + 1));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTxV;
        TextView pageTxV;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxV = itemView.findViewById(R.id.title_tx_v);
            pageTxV = itemView.findViewById(R.id.page_tx_v);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
