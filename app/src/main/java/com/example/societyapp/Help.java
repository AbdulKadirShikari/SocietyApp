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

public class Help extends AppCompatActivity {

    RecyclerView rvhelpId;
    HelpAdapter helpAdapter;
    SharedPreferences predetail;
    String sid;
    ArrayList<HelpModel> helpModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.helptoolbar);
        toolbar.setTitle("Help Desk");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvhelpId=(RecyclerView) findViewById(R.id.rvhelpId);
        predetail=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=predetail.getString("loginsid","loginsid");

        helpDisk();
        rvhelpId.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));


    }
    public void helpDisk() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllHelpDeskContacts";

        Log.e("mkr", "Login: ");
        final ArrayList<String> SliderImage = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Onresponse", response.toString());
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int helpdeskstatus=jsonObject.getInt("status");
                    String helpmessage=jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (helpdeskstatus==200){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObject = jsonArray.getJSONObject(i);

                            String person_name=dataObject.getString("person_name");
                            String person_contact=dataObject.getString("person_contact");
                            String person_type=dataObject.getString("person_type");
                            HelpModel helpModel=new HelpModel();

                            helpModel.person_name=person_name;
                            helpModel.person_contact=person_contact;
                            helpModel.person_type=person_type;

                            helpModels.add(helpModel);

                        }
                    }
                    Log.e("image size","print image size"+helpModels.size());

                    helpAdapter=new HelpAdapter(Help.this,helpModels);
                    rvhelpId.setAdapter(helpAdapter);

                } catch (JSONException e) {
                    Log.e("Exaption","Exaption"+e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Onerror", error.toString());
                dialog.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sid",sid);
                Log.e("parmashelp","helpparm"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
