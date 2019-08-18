package com.example.reading.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading.R;
import com.example.reading.ReadingActivity;
import com.example.reading.view.BookPageBezierHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList;
    private LayoutInflater inflater;
    private int progress;

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
                ReadingActivity readingActivity=   (ReadingActivity)context;
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
                        Toast.makeText(context, "语音朗读", Toast.LENGTH_SHORT).show();
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
