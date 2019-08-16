package com.example.reading;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BaseLisViewAdapter extends BaseAdapter {
    private static final String TAG = "BaseLisViewAdapter";

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

        final BookListResult.Book book = books.get(position);
        viewHolder.tvBookname.setText(book.getBookname());

        //点击下载
        final ViewHolder finalViewHolder = viewHolder;
        //根据文件是否存在来决定按钮上的显示的文本(点击下载/开始阅读)
        final String filePath =context.getFilesDir()+ File.separator+ book.getBookname() + ".txt";
        File file=new File(filePath);
        if (file.exists())
        {
            finalViewHolder.btnDownload.setText("开始阅读");
        }
        else
        {
            finalViewHolder.btnDownload.setText("点击下载");
        }
        viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = finalViewHolder.btnDownload.getText().toString();
                if ("点击下载".equals(btnText))
                {
                    AsyncHttpClient client=new AsyncHttpClient();
                    //设置不压缩,否则无法检测文件的大小,就不能显示正确的下载进度

                    client.addHeader("Accept-Encoding","identity");
                    client.get(book.getBookfile(), new FileAsyncHttpResponseHandler(new File(filePath)) {
                        //下载失败
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            finalViewHolder.btnDownload.setText("下载失败");
                        }

                        //下载成功
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            finalViewHolder.btnDownload.setText("开始阅读");
                            Log.d(TAG, "onSuccess: "+file.getAbsolutePath());
                        }

                        //下载进度
                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            finalViewHolder.btnDownload.setText((bytesWritten*100/totalSize)+"%");
                        }
                    });
                }
                else if ("开始阅读".equals(btnText))
                {
                        //todo 开始阅读
                    Toast.makeText(context, "开始阅读", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
