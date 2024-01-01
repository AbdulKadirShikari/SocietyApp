package com.example.societyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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

class UpcomingEventAdapter extends RecyclerView.Adapter {
    public Context context;
    public String amount;
    private final ArrayList<UpcomingEventModel> users;


    public UpcomingEventAdapter(Context context, ArrayList<UpcomingEventModel> users) {
       this.context=context;
       this.users=users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.upcoming_event_fragment,null);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      UpcomingEventModel user=users.get(position);
      MyHolder myHolder=(MyHolder)holder;
      myHolder.payment.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              UPI_Payment(amount);

          }
      });

      if(user.eventimage.equals("null")||user.eventimage.isEmpty()){
          myHolder.image.setVisibility(View.GONE);
      }else {
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+user.eventimage).into(myHolder.image);
      }if (user.discription.equals("null")){
          myHolder.name.setVisibility(View.GONE);
        }else {
            myHolder.name.setText(user.discription);
        }if (user.payment.equals("null")) {
          myHolder.payment.setVisibility(View.GONE);
        }else {
            myHolder.payment.setText("Pay " + user.payment);
        }
            myHolder.date.setText(user.date);
          myHolder.time.setText(user.time);
      if (user.detail.equals("null")){
          myHolder.detail.setVisibility(View.GONE);
        }else {
            myHolder.detail.setText(user.detail);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

     class MyHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,time;
        public TextView detail,date;
        public Button payment;


         public MyHolder(@NonNull View itemView  ) {
             super(itemView);

         image=itemView.findViewById(R.id.imgeventId);
         name=itemView.findViewById(R.id.tvdiscriptionId);
         payment=itemView.findViewById(R.id.btnPayId);
         detail=itemView.findViewById(R.id.tvtitleId);
         time=itemView.findViewById(R.id.tvtimeId);
         date=itemView.findViewById(R.id.tvDateId);

         }
     }
    public void UPI_Payment(String Amount) {
        try {
            String payeeAddress = "8435146445@ybl";
            String payeeName = "Yatnesh Nigam";
            amount = Amount;
            String currencyUnit = "INR";
            /*Uri uri = Uri.parse("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName + "&tn=" + transactionNote +
                    "&am=" + amount + "&cu=" + currencyUnit);
            Log.e("Log", "onClick: uri: " + uri);
            Intent intent = new Intent(ACTION_VIEW, uri);*/
            /*context.startActivityForResult(1,intent);*/
            Uri uri=Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",payeeAddress)
                    .appendQueryParameter("pn",payeeName)
                    .appendQueryParameter("am",amount)
                    .appendQueryParameter("currency",currencyUnit).build();
            Intent upi_payment=new Intent(Intent.ACTION_VIEW);
            upi_payment.setData(uri);
            Intent chosser=new Intent(Intent.createChooser(upi_payment,"Pay with"));
            if (null!=chosser.resolveActivity(context.getPackageManager())){
                ((Activity)context).startActivityForResult(chosser,1);
            }
        } catch (Exception e) {
            Log.e("errorrr","errorrr"+e);
            e.printStackTrace();
        }
    }
}

