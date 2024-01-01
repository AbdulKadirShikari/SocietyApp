package com.example.societyapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static android.content.Context.MODE_PRIVATE;

public class UpcomimgEvent extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView rvfirstId;
    UpcomingEventAdapter upcomingEventAdapter;
    ArrayList<UpcomingEventModel> users=new ArrayList<>();
    SharedPreferences eventsid;
    String sid;

    public static UpcomimgEvent newInstance() {
        return new UpcomimgEvent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.upcomimg_event,container,false);
        rvfirstId=view.findViewById(R.id.rvfirsteId);
        eventsid=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=eventsid.getString("loginsid","loginsid");
        eventitemshowlist();
        rvfirstId.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    public static UpcomimgEvent newInstance(int index) {
        UpcomimgEvent fragment = new UpcomimgEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void eventitemshowlist() {
         final ProgressDialog dialog=ProgressDialog.show(getContext(),"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllUpCommingEvents";

        Log.e("mkr", "Login: ");
        final ArrayList<String> SliderImage = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Onresponse", response.toString());

                try {
                      dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    users.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String url = dataObject.getString("image");
                        String discriptn=dataObject.getString("detail");
                        String amount=dataObject.getString("amount");
                        String date=dataObject.getString("start_date");
                        String time=dataObject.getString("time");
                        String detail=dataObject.getString("event_title");
                        UpcomingEventModel user=new UpcomingEventModel();
                        user.eventimage=url;
                        user.detail=detail;
                        user.date=date;
                        user.time=time;
                        user.discription=discriptn;
                        user.payment=amount;

                        users.add(user);

                    }
                    Log.e("image size","print image size"+users.size());

                    upcomingEventAdapter =new UpcomingEventAdapter(getActivity(),users);
                    rvfirstId.setAdapter(upcomingEventAdapter);

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
                Log.e("parmasevent","eventparm"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}