
package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final ArrayList<NoticeModel> noticeModels;

    public NoticeAdapter(Context context, ArrayList<NoticeModel> noticeModels) {
        this.context=context;
        this.noticeModels=noticeModels;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.noticeboard,null);

        MyHolder myHolder=new NoticeAdapter.MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      NoticeModel noticeModel=noticeModels.get(position);

      MyHolder myHolder=(MyHolder)holder;
        myHolder.start_time.setText(noticeModel.start_time);
        myHolder.event_date.setText(noticeModel.event_date);
        myHolder.notice_text.setText(noticeModel.notice_text);
        myHolder.end_time.setText(noticeModel.end_time);
        myHolder.created_date.setText(noticeModel.created_date);


    }

    @Override
    public int getItemCount() {
        return noticeModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView start_time,event_date, notice_text, end_time,created_date ;

        public MyHolder(View view) {
            super(view);
            start_time=view.findViewById(R.id.start_time);
            event_date=view.findViewById(R.id.event_date);
            notice_text=view.findViewById(R.id.notice_text);
            end_time=view.findViewById(R.id.end_time);
            created_date=view.findViewById(R.id.created_date);

        }
    }
}
