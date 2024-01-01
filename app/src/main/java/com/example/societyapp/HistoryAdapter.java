package com.example.societyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final ArrayList<HistoryModel> historyModels;
    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModels) {
        this.context=context;
        this.historyModels=historyModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_history,null);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryModel historyModel=historyModels.get(position);
        MyHolder myHolder=(MyHolder)holder;
        myHolder.DateId.setText(historyModel.date);
        myHolder.monthId.setText(
                historyModel.month
        );

        /*Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        myHolder.monthId.setText(month_name);*/

    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {

        public TextView DateId;
        public TextView btnpId,monthId;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            DateId=(TextView)itemView.findViewById(R.id.DateId);
            btnpId=(TextView) itemView.findViewById(R.id.btnpId);
            monthId=(TextView) itemView.findViewById(R.id.monthId);

            /*btnpId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,Pay.class);
                    context.startActivity(intent);
                }
            });*/


        }
    }
}
