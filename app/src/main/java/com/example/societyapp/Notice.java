package com.example.societyapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notice extends AppCompatActivity {
    RecyclerView rvnoticeId;
    NoticeAdapter noticeAdapter;
    SharedPreferences noticeboard;
    String sid;
    ArrayList<NoticeModel> noticeModels=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = findViewById(R.id.noticetoolbar);
        toolbar.setTitle("Notice");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvnoticeId=(RecyclerView) findViewById(R.id.rvnoticeId);
        noticeboard=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=noticeboard.getString("loginsid","loginsid");
         Log.e("getsid","getsid"+sid);


        noticeBoard();
        rvnoticeId.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));


    }
    public void noticeBoard() {
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllNotice";

        Log.e("mkr", "Login: ");

        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);


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
                        String notice_text = dataObject.getString("notice_text");
                        String start_time=dataObject.getString("start_time");
                        String end_time=dataObject.getString("end_time");
                        String event_date=dataObject.getString("event_date");
                        String created_date=dataObject.getString("created_date");

                        NoticeModel noticeModel=new NoticeModel();
                        noticeModel.notice_text=notice_text;
                        noticeModel.start_time=start_time;
                        noticeModel.end_time=end_time;
                        noticeModel.event_date=event_date;
                        noticeModel.created_date=created_date;

                        noticeModels.add(noticeModel);

                    }
                    Log.e("image size","print image size"+noticeModels.size());

                    noticeAdapter=new NoticeAdapter(Notice.this,noticeModels);
                    rvnoticeId.setAdapter(noticeAdapter);

                } catch (JSONException e) {
                    Log.e("Exaption","Exaption"+e);
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
                Log.e("parmasnotice","paramnotice"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}