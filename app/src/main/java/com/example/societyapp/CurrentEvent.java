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

public class CurrentEvent extends Fragment {

    private CurrentEventViewModel mViewModel;
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView rvsecondId;
    CurrentEventAdapter currentEventAdapter;

    SharedPreferences eventsecondsid;
    String sid;


    ArrayList<CurrentEventViewModel> currentEventViewModels=new ArrayList<>();
    public static CurrentEvent newInstance() {
        return new CurrentEvent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.current_event,container,false);
        rvsecondId=view.findViewById(R.id.rvsecondId);
        eventsecondsid=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=eventsecondsid.getString("loginsid","loginsid");
        CurrentItemShowList();
        rvsecondId.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        return view;    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CurrentEventViewModel.class);
        // TODO: Use the ViewModel
    }
    public static CurrentEvent newInstance(int index) {
        CurrentEvent fragment = new CurrentEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void CurrentItemShowList() {
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
                    currentEventViewModels.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        String urlsecond = dataObject.getString("image");
                        String discriptnsecond=dataObject.getString("detail");
                      //  String amount=dataObject.getString("amount");
                        String datesecond=dataObject.getString("start_date");
                        String timesecond=dataObject.getString("time");
                        String detailsecond=dataObject.getString("event_title");
                        CurrentEventViewModel currentEventViewModel=new CurrentEventViewModel();
                        currentEventViewModel.eventimagesecond=urlsecond;
                        currentEventViewModel.detailsecond=detailsecond;
                        currentEventViewModel.datesecond=datesecond;
                        currentEventViewModel.timesecond=timesecond;
                        currentEventViewModel.discriptionsecond=discriptnsecond;
                      //  currentEventViewModel.payment=amount;

                        currentEventViewModels.add(currentEventViewModel);

                    }
                    Log.e("image size","print image size"+currentEventViewModels.size());

                    currentEventAdapter =new CurrentEventAdapter(getActivity(),currentEventViewModels);
                    rvsecondId.setAdapter(currentEventAdapter);

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
                params.put("event_type","1");
                Log.e("second","second"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
