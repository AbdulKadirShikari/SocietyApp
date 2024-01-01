package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    List<NotificationModel>notificationModels=new ArrayList<>();
    Context context;

    public NotificationAdapter(Context context,List<NotificationModel>notificationModels){
        this.context=context;
        this.notificationModels=notificationModels;
    }
    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_notification_show,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        NotificationModel notificationModel=notificationModels.get(position);
        holder.wholeTextId.setText(notificationModel.notificationtext);
        holder.dateshowID.setText(notificationModel.dateshow);
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView wholeTextId,dateshowID;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wholeTextId=itemView.findViewById(R.id.wholeTextId);
            dateshowID=itemView.findViewById(R.id.dateshowID);
        }
    }
}
