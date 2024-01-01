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

public class Rules extends AppCompatActivity {
    RecyclerView rvRulesId;
    RulesAdapter rulesAdapter;
    ArrayList<RulesModel> rulesModels=new ArrayList<>();
    SharedPreferences getRules;
    String sid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = findViewById(R.id.rulestoolbar);
        toolbar.setTitle("Rules");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvRulesId=(RecyclerView) findViewById(R.id.rvRulesId);
        getRules=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=getRules.getString("loginsid","loginsid");


        RulesList();
        rvRulesId.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));


    }
    public void RulesList() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        String url = "http://placementsadda.com/EazyKaam/Login";
        final String URL = "http://placementsadda.com/Society/getAllRulls";

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

                       String rules=dataObject.getString("rules");
                        String created_date=dataObject.getString("created_date");
                        RulesModel rulesModel=new RulesModel();

                        rulesModel.createdtitle=rules;
                        rulesModel.created_date=created_date;

                        rulesModels.add(rulesModel);


                    }
                    rulesAdapter=new RulesAdapter(Rules.this,rulesModels);
                    rvRulesId.setAdapter(rulesAdapter);

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
                Log.e("parmasrule","ruleparm"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}