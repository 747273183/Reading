package com.example.reading.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading.R;
import com.example.reading.ReadingActivity;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.client.utils.URIUtils;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList;
    private LayoutInflater inflater;
    private int progress;
    private TextToSpeech tts;

    private static final String TAG = "SettingRecyclerAdapter";

    public SettingRecyclerAdapter(Context context, List<String> stringList) {

        this.context = context;
        this.stringList = stringList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflate = inflater.inflate(R.layout.item_setting_recyclerview, viewGroup, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        String s = stringList.get(i);
        myViewHolder.tvName.setText(s);

        //显示/隐藏 设置view(1.添加书签 2.读取书签 3.设置背景  4.语音朗读 5.跳转进度)
        myViewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ReadingActivity readingActivity=   (ReadingActivity)context;
                SharedPreferences sharedPreferences=context.getSharedPreferences("bookmarks",Context.MODE_PRIVATE);
                switch (i)
                {
                    case 0:
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        //设置某本书读取的进度
                        edit.putInt(readingActivity.filePath,readingActivity.currentLength);
                        edit.commit();
                        Toast.makeText(readingActivity, "添加书签成功"+readingActivity.currentLength, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        progress = sharedPreferences.getInt(readingActivity.filePath, 0);
                        Toast.makeText(context, "读取书签"+ progress, Toast.LENGTH_SHORT).show();
                        readingActivity.openBook(progress,R.drawable.book_bg);

                        break;
                    case 2:
                        readingActivity.openBook(progress,R.drawable.book_bg2);
                        Toast.makeText(context, "设置背景成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(context, "开始语音朗读", Toast.LENGTH_SHORT).show();
                        if (tts==null)
                        {
                            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status==TextToSpeech.SUCCESS)
                                    {
                                        int result = tts.setLanguage(Locale.CHINA);
                                        if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
                                        {
                                            Log.d(TAG, "onInit: language is not available.");
                                            Uri uri=Uri.parse("http://acj2.pc6.com/pc6_soure/2017-6/com.iflytek.vflynote_208.apk");
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            context.startActivity(intent);
                                        }
                                        else
                                        {
                                            Log.d(TAG, "onInit: init success");
                                            tts.speak(readingActivity.bookPageBezierHelper.getCurrentPageContent(), TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                    else
                                    {
                                        Log.d(TAG, "onInit: init error");
                                    }
                                }
                            });
                        }
                        else
                        {
                            if (tts.isSpeaking())
                            {
                                tts.stop();
                            }
                            else
                            {
                                tts.speak(readingActivity.bookPageBezierHelper.getCurrentPageContent(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }

                        break;
                    case 4:

                        Toast.makeText(context, "跳转进度", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }



    static
    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView tvName;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}
