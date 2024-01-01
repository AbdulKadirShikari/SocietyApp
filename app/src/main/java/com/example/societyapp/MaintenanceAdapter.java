package com.example.societyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.getIntentOld;
import static android.content.Intent.makeRestartActivityTask;

public class MaintenanceAdapter extends RecyclerView.Adapter {
    public final Context context;
    public final ArrayList<MaintenanceModel> maintenanceModels;
    public String amount;
    Dialog myDialog;

    public MaintenanceAdapter(Context context, ArrayList<MaintenanceModel> maintenanceModels) {
        this.context=context;
        this.maintenanceModels=maintenanceModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.maintenancelist,null);

        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MaintenanceModel maintenanceModel=maintenanceModels.get(position);
        MyHolder myHolder=(MyHolder)holder;
        myHolder.DateId.setText(maintenanceModel.created_date);
        myHolder.btnpId.setText(maintenanceModel.min_amount);
        myHolder.monthId.setText(maintenanceModel.month);
        amount=maintenanceModel.min_amount;
        myHolder.btnPayId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UPI_Payment(amount);
            }
        });
        myDialog=new Dialog(context);
        myHolder.showAlldetailsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDetails(view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return maintenanceModels.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        Button btnPayId;
        public TextView DateId,showAlldetailsID;
        public TextView btnpId,monthId;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            DateId=(TextView)itemView.findViewById(R.id.DateId);
            showAlldetailsID=(TextView)itemView.findViewById(R.id.showAlldetailsID);
            btnPayId=(Button) itemView.findViewById(R.id.btnPayId);
            btnpId=(TextView) itemView.findViewById(R.id.btnpId);
            monthId=(TextView) itemView.findViewById(R.id.monthId);

            Log.e("amount","amount"+amount);

            btnpId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,Pay.class);
                    context.startActivity(intent);
                }
            });
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
/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("Result","Data");
        if(data!=null && callResult!=null) {
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                callResult.success("Success");
            } else {
                callResult.success("Failed");
            }
        }else{
            Log.d("Result","Data = null (User canceled)");
            callResult.success("User Cancelled");
        }
        super.(Activity)onActivityResult(requestCode,resultCode,data);
    }*/

    public void ShowDetails(View view){
        ImageView txtclose;
        myDialog.setContentView(R.layout.custompopup);
        txtclose=(ImageView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
