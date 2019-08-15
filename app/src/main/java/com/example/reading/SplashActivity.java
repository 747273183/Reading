package com.example.reading;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    public static final int CODE = 1101;
    public static int time = 3000;
    public static final int STEP_TIME = 1000;
    @BindView(R.id.btn_skip)
    Button btnSkip;

    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {

                     btnSkip.setText((time/STEP_TIME)+"");
                     time=time-STEP_TIME;
                     if (time>=0)
                     {

                         handler.postDelayed(runnable,STEP_TIME);
                     }
                     else
                     {
                         MainActivity.start(SplashActivity.this);
                     }
                 }
             });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        handler.postDelayed(runnable,time);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @OnClick(R.id.btn_skip)
    public void onClick() {
        handler.removeCallbacks(runnable);
        MainActivity.start(this);
    }



}
