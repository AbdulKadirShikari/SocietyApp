package com.example.societyapp;

import androidx.lifecycle.ViewModelProviders;

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

public class PastEvent extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PastEventViewModel mViewModel;
    PastEventAdapter pastEventAdapter;
    RecyclerView rvthirdId;
    ArrayList<PastEventViewModel> pastEventViewModels=new ArrayList<>();
    SharedPreferences pastsid;
    String sid;


    public static PastEvent newInstance() {
        return new PastEvent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.past_event,container,false);
        rvthirdId=view.findViewById(R.id.rvthirdId);
        pastsid=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=pastsid.getString("loginsid","loginsid");
        pastEventList();
        rvthirdId.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        return view;    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PastEventViewModel.class);
        // TODO: Use the ViewModel
    }

    public static PastEvent newInstance(int index) {
        PastEvent fragment = new PastEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void pastEventList() {
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
                    pastEventViewModels.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String urlthird = dataObject.getString("image");
                        String discriptnthird=dataObject.getString("detail");
                        String amountthird=dataObject.getString("amount");
                        String datethird=dataObject.getString("start_date");
                        String timethird=dataObject.getString("time");
                        String detailthird=dataObject.getString("event_title");
                        PastEventViewModel pastEventViewModel=new PastEventViewModel();
                        pastEventViewModel.eventimagethird=urlthird;
                        pastEventViewModel.detailthird=detailthird;
                        pastEventViewModel.datethird=datethird;
                        pastEventViewModel.timethird=timethird;
                        pastEventViewModel.discriptionthird=discriptnthird;

                        pastEventViewModels.add(pastEventViewModel);

                    }
                    Log.e("image size","print image size"+pastEventViewModels.size());

                    pastEventAdapter =new PastEventAdapter(getActivity(),pastEventViewModels);
                    rvthirdId.setAdapter(pastEventAdapter);

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
                params.put("event_type","0");
                Log.e("parmasevent","eventparm"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
