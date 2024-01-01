package com.example.societyapp.ui.share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.societyapp.R;
import com.example.societyapp.ui.home.SliderImageModel;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    ViewPager viewPager;
    SpringDotsIndicator springDotsIndicator;
    SharedPreferences userID_Shared;
    String sid;
    ArrayList<SliderImageModel> imageList=new ArrayList<SliderImageModel>();
    CustomPagerAdapter myCustomPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        viewPager =  findViewById(R.id.viewPager);
        springDotsIndicator = findViewById(R.id.spring_dots_indicator);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80,0,80,0);
        viewPager.setPageMargin(20);

        userID_Shared=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=userID_Shared.getString("loginsid","loginsid");
        Log.e("userunique_id","userunique_id ::"+sid);

        SliderImage();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


    }
    public void SliderImage() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllSliderimages";

        Log.e("mkr", "Login: ");
        final ArrayList<String> SliderImage = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("sliderimage", response.toString());

                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    int sliderstatus=jsonObject.getInt("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (sliderstatus==200){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            String url = dataObject.getString("image");
                            SliderImageModel sliderImageModel=new SliderImageModel();
                            sliderImageModel.sliderimage=url;
                            imageList.add(sliderImageModel);

                            Log.e("sliderimage",url);
                        }
                    }
                    Log.e("image size","print image size"+imageList.size());
                    viewPager = (ViewPager)findViewById(R.id.viewPager);

                    myCustomPagerAdapter = new CustomPagerAdapter(getApplicationContext(),imageList);
                    viewPager.setAdapter(myCustomPagerAdapter);

                    springDotsIndicator.setViewPager(viewPager);


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
                Log.e("slidersid","sid"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
