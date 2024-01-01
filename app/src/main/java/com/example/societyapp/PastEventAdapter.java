package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class PastEventAdapter extends RecyclerView.Adapter {
    public Context context;
    public String amount;
    private final ArrayList<PastEventViewModel> pastEventViewModels;
    public PastEventAdapter(Context context, ArrayList<PastEventViewModel>pastEventViewModels) {
        this.context=context;
        this.pastEventViewModels=pastEventViewModels;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.past_event_fragment,null);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PastEventViewModel pastEventViewModel=pastEventViewModels.get(position);
        MyHolder myHolder=(MyHolder)holder;
        if(pastEventViewModel.eventimagethird.equals("null")||pastEventViewModel.eventimagethird.isEmpty()){
            myHolder.imagethird.setVisibility(View.GONE);
        }else {
            Picasso.get().load("http://placementsadda.com/Society/uploads/"+pastEventViewModel.eventimagethird).into(myHolder.imagethird);
        }if (pastEventViewModel.discriptionthird.equals("null")){
            myHolder.namethird.setVisibility(View.GONE);
        }else {
            myHolder.namethird.setText(pastEventViewModel.discriptionthird);
        }
        myHolder.datethird.setText(pastEventViewModel.datethird);
        myHolder.timethird.setText(pastEventViewModel.timethird);
        if (pastEventViewModel.detailthird.equals("null")){
            myHolder.detailthird.setVisibility(View.GONE);
        }else {
            myHolder.detailthird.setText(pastEventViewModel.detailthird);
        }
    }

    @Override
    public int getItemCount() {
        return pastEventViewModels.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView imagethird;
        public TextView namethird, timethird;
        public TextView detailthird, datethird;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imagethird = itemView.findViewById(R.id.imgeventthirdId);
            namethird = itemView.findViewById(R.id.tvdiscriptionthirdId);
            detailthird = itemView.findViewById(R.id.tvtitlethirdId);
            timethird = itemView.findViewById(R.id.tvtimethirdId);
            datethird = itemView.findViewById(R.id.tvDatethirdId);

        }
    }
}
