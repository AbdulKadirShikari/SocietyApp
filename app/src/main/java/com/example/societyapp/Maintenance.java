package com.example.societyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.societyapp.HistoryAdapter;
import com.example.societyapp.HistoryModel;
import com.example.societyapp.MaintenanceAdapter;
import com.example.societyapp.MaintenanceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Maintenance extends AppCompatActivity {
    RecyclerView rvmainId,rvmain2Id;
    MaintenanceAdapter maintenanceAdapter;
    HistoryAdapter historyAdapter;
    String sid;
    ArrayList<HistoryModel>historyModels=new ArrayList<>();
    ArrayList<MaintenanceModel>maintenanceModels=new ArrayList<>();
    SharedPreferences predetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        Toolbar toolbar = findViewById(R.id.maintaincetoolbar);
        toolbar.setTitle("Maintaince");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvmainId=(RecyclerView)findViewById(R.id.rvmainId);
        rvmain2Id=(RecyclerView)findViewById(R.id.rvmain2Id);

        predetail=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=predetail.getString("loginsid","loginsid");

        HistoryModel historyModel=new HistoryModel();
        historyModel.month="October";
        historyModel.date="12:00Am 12-12-2012";
        historyModels.add(historyModel);

        historyAdapter=new HistoryAdapter(Maintenance.this,historyModels);
        rvmain2Id.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rvmain2Id.setHasFixedSize(true);
        rvmain2Id.setAdapter(historyAdapter);


        maintenance();
        rvmainId.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

    }
    public void maintenance() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getMaintanceCharge";

        Log.e("mkr", "Login: ");
        final ArrayList<String> SliderImage = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Onresponse", response.toString());

                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataObject = jsonArray.getJSONObject(i);

                        String min_amount=dataObject.getString("min_amount");
                        String created_date=dataObject.getString("created_date");
                        String month=dataObject.getString("month");


                        MaintenanceModel maintenanceModel=new MaintenanceModel();

                        maintenanceModel.min_amount=min_amount;
                        maintenanceModel.created_date=created_date;
                        maintenanceModel.month=month;


                        maintenanceModels.add(maintenanceModel);

                    }
                    Log.e("image size","print image size"+maintenanceModels.size());

                    maintenanceAdapter=new MaintenanceAdapter(Maintenance.this,maintenanceModels);
                    rvmainId.setAdapter(maintenanceAdapter);


                } catch (JSONException e) {
                    Log.e("Exeption","Exeption"+e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("Onerror", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sid",sid);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}

