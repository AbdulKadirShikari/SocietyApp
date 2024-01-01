package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ArrayList<HelpModel> helpModels;

    public HelpAdapter(Context context, ArrayList<HelpModel> helpModels) {
        this.context=context;
        this.helpModels=helpModels;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.helpdisklist,null);

        RecyclerView.ViewHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HelpModel helpModel=helpModels.get(position);
       MyHolder myHolder=(MyHolder)holder;
        myHolder.person.setText(helpModel.person_name);
        myHolder.contact.setText(helpModel.person_contact);
        myHolder.type.setText(helpModel.person_type);

    }

    @Override
    public int getItemCount() {
        return helpModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

      public TextView person,type,date,contact;

        public MyHolder(View view) {
            super(view);
            person=view.findViewById(R.id.securitynameId);
            contact=view.findViewById(R.id.securityphoneId);
            type=view.findViewById(R.id.jobId);
        }
    }
}



