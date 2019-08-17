package com.example.reading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.reading.adapter.BaseLisViewAdapter;
import com.example.reading.entity.BookListResult;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.lv)
    ListView lv;
    private String url = "https://www.imooc.com/api/teacher?type=10";
    private static final String TAG = "MainActivity";
    private BaseLisViewAdapter baseLisViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("书籍列表");



        //发起网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //将获得的数据封装在books集合对象中
                String response = new String(responseBody);
                Gson gson = new Gson();
                BookListResult bookListResult = gson.fromJson(response, BookListResult.class);
                List<BookListResult.Book> books = bookListResult.getData();

                //加载书籍列表
                baseLisViewAdapter=new BaseLisViewAdapter(MainActivity.this,books);
                lv.setAdapter(baseLisViewAdapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
