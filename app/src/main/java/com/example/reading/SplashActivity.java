package com.example.reading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    public static final int CODE = 1101;
    public static final int TOTAL_TIME = 3000;
    public static final int STEP_TIME = 1000;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //1.触发消息循环
        handler = new MyHandler(this);
        Message msg = Message.obtain();
        msg.what = CODE;
        msg.arg1 = TOTAL_TIME;
        handler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(CODE);
    }

    @OnClick(R.id.btn_skip)
    public void onClick() {

        MainActivity.start(this);
        handler.removeMessages(CODE);
    }

    public static class MyHandler extends Handler {
        public final WeakReference<SplashActivity> mWeakReference;

        public MyHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        //2.处理消息
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();

            if (msg.what == CODE) {
                if (activity != null) {
                    //修改按钮上显示的文本
                    activity.btnSkip.setText(msg.arg1/STEP_TIME+"秒,跳过");
                    //修改剩余倒计时时间
                    Message message=Message.obtain();
                    message.what=CODE;
                    message.arg1=msg.arg1- STEP_TIME;
                    if (message.arg1>0)
                    {
                        //发送消息
                        sendMessageDelayed(message,STEP_TIME);
                    }
                    else
                    {
                        MainActivity.start(activity);
                    }


                }
            }
        }

    }
}
