package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CurrentEventAdapter extends RecyclerView.Adapter {
    public Context context;
    public String amount;
    private final ArrayList<CurrentEventViewModel> currentEventViewModels;

    public CurrentEventAdapter(Context context, ArrayList<CurrentEventViewModel> currentEventViewModels) {
        this.currentEventViewModels=currentEventViewModels;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.current_event_fragment,null);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CurrentEventViewModel currentEventViewModel=currentEventViewModels.get(position);
        MyHolder myHolder=(MyHolder)holder;
        if(currentEventViewModel.eventimagesecond.equals("null")||currentEventViewModel.eventimagesecond.isEmpty()){
            myHolder.imagesecond.setVisibility(View.GONE);
        }else {
            Picasso.get().load("http://placementsadda.com/Society/uploads/"+currentEventViewModel.eventimagesecond).into(myHolder.imagesecond);
        }if (currentEventViewModel.discriptionsecond.equals("null")){
            myHolder.namesecond.setVisibility(View.GONE);
        }else {
            myHolder.namesecond.setText(currentEventViewModel.discriptionsecond);
        }
        myHolder.datesecond.setText(currentEventViewModel.datesecond);
        myHolder.timesecond.setText(currentEventViewModel.timesecond);
        if (currentEventViewModel.detailsecond.equals("null")){
            myHolder.detailsecond.setVisibility(View.GONE);
        }else {
            myHolder.detailsecond.setText(currentEventViewModel.detailsecond);
        }

    }

    @Override
    public int getItemCount() {
        return currentEventViewModels.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView imagesecond;
        public TextView namesecond, timesecond;
        public TextView detailsecond, datesecond;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imagesecond = itemView.findViewById(R.id.imgeventsecondId);
            namesecond = itemView.findViewById(R.id.tvdiscriptionsecondId);
            detailsecond = itemView.findViewById(R.id.tvtitlesecondId);
            timesecond = itemView.findViewById(R.id.tvtimesecondId);
            datesecond = itemView.findViewById(R.id.tvDatesecondId);

        }
    }}
