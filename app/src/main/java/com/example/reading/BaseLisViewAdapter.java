package com.example.reading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseLisViewAdapter extends BaseAdapter {

    private Context context;
    private List<BookListResult.Book> books;
    private final LayoutInflater inflater;


    public BaseLisViewAdapter(Context context, List<BookListResult.Book> books) {
        this.context = context;
        this.books = books;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public BookListResult.Book getItem(int position) {

        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BookListResult.Book book = books.get(position);
        viewHolder.tvBookname.setText(book.getBookname());

        return convertView;
    }


    static
    class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_bookname)
        TextView tvBookname;
        @BindView(R.id.btn_download)
        Button btnDownload;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
