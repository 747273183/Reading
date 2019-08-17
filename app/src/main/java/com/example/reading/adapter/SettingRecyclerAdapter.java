package com.example.reading.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reading.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList;
    private LayoutInflater inflater;

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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String s = stringList.get(i);
        myViewHolder.tvName.setText(s);
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
