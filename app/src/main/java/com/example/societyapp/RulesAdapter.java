package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RulesAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final ArrayList<RulesModel> rulesModels;

    public RulesAdapter(Context context, ArrayList<RulesModel> rulesModels) {
        this.context=context;
        this.rulesModels=rulesModels;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.showrules,null);

        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RulesModel rulesModel=rulesModels.get(position);
        MyHolder myHolder=(MyHolder)holder;
        myHolder.tvrulesId.setText("* "+rulesModel.createdtitle);


    }

    @Override
    public int getItemCount() {
        return rulesModels.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {

       public TextView tvrulesId,createdDateId;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvrulesId=(TextView)itemView.findViewById(R.id.tvrulesId);


        }
    }
}