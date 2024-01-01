package com.example.societyapp.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.societyapp.DiscussionFormActivity;
import com.example.societyapp.Event;
import com.example.societyapp.Help;
import com.example.societyapp.Maintenance;
import com.example.societyapp.Notice;
import com.example.societyapp.R;
import com.example.societyapp.Rules;
import com.example.societyapp.SocietyServiceActivity;
import com.example.societyapp.Staff_members_Activity;
import com.example.societyapp.ui.share.CustomPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    LinearLayout layoutcomplainId,layoutmemberId,layoutrilesId,layouthelpId,layoutmaintainceId,layoutnoticeId;

    LinearLayout layouteventId,layoutdiscussId;
    ViewPager viewPager;
    SpringDotsIndicator springDotsIndicator;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    CustomPagerAdapter myCustomPagerAdapter;
    ArrayList<String> image=new ArrayList<>();
    ArrayList<SliderImageModel> imageList=new ArrayList<SliderImageModel>();
    SharedPreferences userID_Shared;
    String sid;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        layoutcomplainId=root.findViewById(R.id.layoutcomplainId);
        layoutnoticeId=root.findViewById(R.id.layoutnoticeId);
        layoutmaintainceId=root.findViewById(R.id.layoutmaintainceId);
        layouthelpId=root.findViewById(R.id.layouthelpId);
        layoutrilesId=root.findViewById(R.id.layoutrilesId);
        layoutdiscussId=root.findViewById(R.id.layoutdiscussId);
        layoutmemberId=root.findViewById(R.id.layoutmemberId);
        layouteventId=root.findViewById(R.id.layouteventId);
        userID_Shared=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=userID_Shared.getString("loginsid","loginsid");
        viewPager =root.findViewById(R.id.viewPager);
        Log.e("userunique_id","userunique_id ::"+sid);
        userID_Shared=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=userID_Shared.getString("loginsid","loginsid");
        Log.e("userunique_id","userunique_id ::"+sid);

        springDotsIndicator = root.findViewById(R.id.spring_dots_indicator);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80,0,80,0);
        viewPager.setPageMargin(20);

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


        layouteventId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Event.class);
                startActivity(intent);
            }
        });

        layouthelpId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Help.class);
                startActivity(intent);
            }
        });
        layoutmaintainceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Maintenance.class);
                startActivity(intent);
            }
        });

        layoutnoticeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Notice.class);
                startActivity(intent);
            }
        });


        layoutcomplainId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent services=new Intent(getActivity(), SocietyServiceActivity.class);
                startActivity(services);

            }
        });

        layoutrilesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent services=new Intent(getActivity(), Rules.class);
                startActivity(services);
            }
        });

        layoutmemberId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent staffmemberslist=new Intent(getActivity(), Staff_members_Activity.class);
                startActivity(staffmemberslist);
            }
        });

        layoutdiscussId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discussform=new Intent(getActivity(), DiscussionFormActivity.class);
                startActivity(discussform);
            }
        });

        return root;
    }


    public void SliderImage() {
        final ProgressDialog dialog=ProgressDialog.show(getActivity(),"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllSliderimages";

        Log.e("mkr", "Login: ");
        final ArrayList<String> SliderImage = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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


                    myCustomPagerAdapter = new CustomPagerAdapter(getActivity(),imageList);
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