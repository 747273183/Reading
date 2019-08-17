package com.example.reading;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading.view.BookPageBezierHelper;
import com.example.reading.view.BookPageView;

import java.io.IOException;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingActivity extends AppCompatActivity {

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.bookPageView)
    BookPageView bookPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明的状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明的导航
        }
        setContentView(R.layout.activity_reading);
        ButterKnife.bind(this);

        //设置翻页的宽高
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //设置翻页
        BookPageBezierHelper bookPageBezierHelper = new BookPageBezierHelper(width, height);
        bookPageView.setBookPageBezierHelper(bookPageBezierHelper);

        //设置当前页 下一页
        Bitmap currentPageBitMap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Bitmap nextPageBitMap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        bookPageView.setBitmaps(currentPageBitMap,nextPageBitMap);

        //设置背景图片
        bookPageBezierHelper.setBackground(this,R.drawable.book_bg);
        //设置进度
        bookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                String format = String.format("%.4f", currentLength * 100.0000 / totalLength);
                tvProgress.setText(format+"%");
            }
        });


        //加载书内容
        String filePath = getIntent().getStringExtra("filePath");
        try {
            bookPageBezierHelper.openBook(filePath);//打开
            bookPageBezierHelper.draw(new Canvas(currentPageBitMap));//显示第一页
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
