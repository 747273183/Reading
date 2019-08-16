package com.example.reading;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1101;
    public static int time = 3000;
    public static final int STEP_TIME = 1000;
    @BindView(R.id.btn_skip)
    Button btnSkip;

    private CountDownTimer countDownTimer=new CountDownTimer(time,STEP_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnSkip.setText("跳过("+(millisUntilFinished/STEP_TIME)+"S)");

        }

        @Override
        public void onFinish() {
            MainActivity.start(SplashActivity.this);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);



        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @OnClick(R.id.btn_skip)
    public void onClick() {
        countDownTimer.cancel();
        MainActivity.start(SplashActivity.this);
    }



}