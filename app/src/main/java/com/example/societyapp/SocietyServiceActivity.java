package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.societyapp.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocietyServiceActivity extends AppCompatActivity {
TextInputEditText etcompainBoxId;
Spinner spinnerserviceId;
Button btsubmitComplainId;
String selectedservice,complaintextview,servicesid,sid;
SharedPreferences allservicesshow;
int serviceIdpostion;
List<String>servicecorespondsid=new ArrayList<>();
List<String> StringServicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_service);
        Toolbar toolbar = findViewById(R.id.complaintoolbar);
        toolbar.setTitle("Complaints");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etcompainBoxId=(TextInputEditText)findViewById(R.id.etcompainBoxId);
        spinnerserviceId=(Spinner)findViewById(R.id.spinnerserviceId);
        btsubmitComplainId=(Button)findViewById(R.id.btsubmitComplainId);
        StringServicesList = new ArrayList<>();
        allservicesshow=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=allservicesshow.getString("loginsid","loginsid");
        selectedservice=allservicesshow.getString("loginsid","loginsid");
        servicesid=allservicesshow.getString("loginsid","loginsid");
        RequestShowServices();
        StringServicesList.add("Select");

        spinnerserviceId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                {
                    selectedservice = spinnerserviceId.getItemAtPosition(spinnerserviceId.getSelectedItemPosition()).toString();
                    serviceIdpostion=i;
                    //Toast.makeText(SocietyServiceActivity.this, "serviceid"+serviceIdpostion, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btsubmitComplainId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaintextview=etcompainBoxId.getText().toString();
                if (complaintextview==null||complaintextview.isEmpty()){
                    Toast.makeText(SocietyServiceActivity.this,"Reuired to give Complain",Toast.LENGTH_LONG).show();
                }else {
                    submitservieGenerateComplain();
                }
            }
        });
    }


    public void RequestShowServices(){
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://placementsadda.com/Society/getAllService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("compls","cmpln ::"+response);


                try {
                    dialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    String loginstatus=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    JSONArray logindata=jsonObject.getJSONArray("data");
                  Log.e("compls","Status ::"+response);
                    if (loginstatus.equals(200)){
                        for (int i=0;i<logindata.length();i++) {
                            JSONObject userSuccessData = logindata.getJSONObject(i);
                            String serviceId = userSuccessData.getString("id");
                            String Getservicename = userSuccessData.getString("service_name");
                            String servicedetails = userSuccessData.getString("sdetail");
                            StringServicesList.add(Getservicename);
                            servicecorespondsid.add(serviceId);

                            /*StaffServicesNameListModel staffServicesNameListModel=new StaffServicesNameListModel();
                            staffServicesNameListModel.serviceId=serviceId;
                            staffServicesNameListModel.servicename=Getservicename;
                            staffServicesNameListModels.add(staffServicesNameListModel);*/
                            /*SharedPreferences sharedPreferences = getSharedPreferences("Serviceinformation", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("serviceId", serviceId);
                            editor.putString("Getservicename", Getservicename);
                            editor.putString("servicedetails", servicedetails);
                            editor.commit();*/
                        }
                    }
                    spinnerserviceId.setAdapter(new ArrayAdapter<String>(SocietyServiceActivity.this, android.R.layout.simple_spinner_dropdown_item, StringServicesList));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("error","error::"+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("sid",sid);
                Log.e("paramsendresponce","paramsendresponce"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void submitservieGenerateComplain(){
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://placementsadda.com/Society/new_complaint", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("newcmpln","Status ::"+response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                  //  Log.e("oldcmpln","Status ::"+response);

                    int loginstatus=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    JSONObject logindata=jsonObject.getJSONObject("data");
                    String id=logindata.getString("id");
                    Log.e("loginstatus","loginstatus"+id);
                    if (loginstatus==200){
                        dialog.dismiss();
                        Intent complainservicesubmitt=new Intent(SocietyServiceActivity.this, Society_Dashboard_activity.class);
                        startActivity(complainservicesubmitt);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("error","error::"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("complaint_text",complaintextview);
                params.put("service_id",servicesid);
                params.put("user_id",selectedservice);
                Log.e("complain","param::"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
