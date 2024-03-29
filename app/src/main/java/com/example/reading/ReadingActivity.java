package com.example.reading;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.reading.adapter.SettingRecyclerAdapter;
import com.example.reading.view.BookPageBezierHelper;
import com.example.reading.view.BookPageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingActivity extends AppCompatActivity {

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.bookPageView)
    BookPageView bookPageView;
    @BindView(R.id.setting_view)
    LinearLayout settingView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.seekBar)
    public  SeekBar seekBar;


    private List<String> strings = new ArrayList<>();
    private SettingRecyclerAdapter settingRecyclerAdapter;
    public int currentLength;
    public String filePath;
    public BookPageBezierHelper bookPageBezierHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明的状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明的导航
        }
        setContentView(R.layout.activity_reading);
        ButterKnife.bind(this);

        //打开书
        openBook(0, R.drawable.book_bg);
        //添加设置菜单
        addSettingMenu();

        //进度条值被改变监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //开始改变时(实时改变,影响性能)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                openBook(seekBar.getProgress()*bookPageBezierHelper.mBookBufferLength/100,R.drawable.book_bg);
            }

            //开始拖动时
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //结束拖动时
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                openBook(seekBar.getProgress()*bookPageBezierHelper.mBookBufferLength/100,R.drawable.book_bg);
            }
        });


    }

    private void addSettingMenu() {
        //设置recyclerView的适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        strings.add("添加书签");
        strings.add("读取书签");
        strings.add("设置背景");
        strings.add("语音朗读");
        strings.add("跳转进度");

        settingRecyclerAdapter = new SettingRecyclerAdapter(this, strings);

        recyclerView.setAdapter(settingRecyclerAdapter);


        //显示/隐藏 设置view(1.添加书签 2.读取书签 3.设置背景  4.语音朗读 5.跳转进度)
        bookPageView.setOnUserNeedSettingListener(new BookPageView.OnUserNeedSettingListener() {
            @Override
            public void onUserNeedSetting() {
                settingView.setVisibility(settingView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }


    public void openBook(int progress, int bgResID) {
        //设置翻页的宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //设置翻页
        bookPageBezierHelper = new BookPageBezierHelper(width, height, progress);
        bookPageView.setBookPageBezierHelper(bookPageBezierHelper);

        //设置当前页 下一页
        Bitmap currentPageBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap nextPageBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bookPageView.setBitmaps(currentPageBitMap, nextPageBitMap);

        //设置背景图片
        bookPageBezierHelper.setBackground(this, bgResID);
        //设置进度
        bookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                ReadingActivity.this.currentLength = currentLength;
                String format = String.format("%.4f", currentLength * 100.0000 / totalLength);
                tvProgress.setText(format + "%");
            }
        });

        //加载书内容
        filePath = getIntent().getStringExtra("filePath");
        try {
            bookPageBezierHelper.openBook(filePath);//打开
            bookPageBezierHelper.draw(new Canvas(currentPageBitMap));//显示第一页
            bookPageView.invalidate();//重绘
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void start(Context activity, String filePath) {
        Intent intent = new Intent(activity, ReadingActivity.class);
        intent.putExtra("filePath", filePath);
        activity.startActivity(intent);
    }


}
