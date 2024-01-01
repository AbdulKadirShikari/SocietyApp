package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Locale;
import java.util.Map;
import java.util.logging.Filter;

public class Staff_members_Activity extends AppCompatActivity {
    RecyclerView staffmemberlistrecyler;
    String sid,charText;
    TextInputEditText etsearchmemberId;
    SharedPreferences staffmember;
    StaffMemberAdapter staffMemberAdapter;

   public  List<StaffMemberViewModel>staffMemberViewModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_staff_list);
        Toolbar toolbar = findViewById(R.id.staffmembertoolbar);
        etsearchmemberId=findViewById(R.id.etsearchmemberId);
        staffmemberlistrecyler=(RecyclerView)findViewById(R.id.staffmemberlistrecyler);
        staffmember=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=staffmember.getString("loginsid","loginsid");
        GetStaffMemberAllList();

        etsearchmemberId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              filter(s.toString());
            }
        });



        toolbar.setTitle("Staff List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    void filter(String text){
        ArrayList<StaffMemberViewModel> filteredList = new ArrayList<>();
        for(StaffMemberViewModel item:staffMemberViewModels)
        {
            if(item.staffusername.toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        staffMemberAdapter.filterdList(filteredList);
        staffMemberAdapter.notifyDataSetChanged();
    }


    public void GetStaffMemberAllList(){
        Log.e("tag1","flag1");
       final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(1, "http://placementsadda.com/Society/getAllStaffMember", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    Log.e("stafff","Status ::"+response);
                    JSONObject jsonObject=new JSONObject(response);
                    int loginstatus=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    JSONArray listdata=jsonObject.getJSONArray("data");




                    Log.e("loginstatus","loginstatus"+loginstatus);
                    if (loginstatus==200){
                        dialog.dismiss();
                        for (int i=0;i<listdata.length();i++) {
                            JSONObject userSuccessData = listdata.getJSONObject(i);
                           // String staffmId = userSuccessData.getString("mid");
                            String staffusername = userSuccessData.getString("member_name");
                            String staffmobileno = userSuccessData.getString("contact");
                            String staffflatno = userSuccessData.getString("flatno");
                            String staffaddress = userSuccessData.getString("address");
                            String staffemail = userSuccessData.getString("email");
                            String service_id = userSuccessData.getString("service_id");
                            String staffadhar_no = userSuccessData.getString("adhar_card");
                          //  String staffpassword = userSuccessData.getString("password");
                            String staffimageurl = userSuccessData.getString("image");
                            StaffMemberViewModel staffMemberViewModel=new StaffMemberViewModel();
                         //   staffMemberViewModel.staffmId=staffmId;
                            staffMemberViewModel.staffusername=staffusername;
                            staffMemberViewModel.staffmobileno=staffmobileno;
                            staffMemberViewModel.staffflatno=staffflatno;
                            staffMemberViewModel.staffaddress=staffaddress;
                            staffMemberViewModel.staffemail=staffemail;
                            staffMemberViewModel.service_id=service_id;
                            staffMemberViewModel.staffadhar_no=staffadhar_no;
                            staffMemberViewModel.staffimageurl=staffimageurl;
                            staffMemberViewModels.add(staffMemberViewModel);
                        }
                    }
                     staffMemberAdapter=new StaffMemberAdapter(Staff_members_Activity.this,staffMemberViewModels);
                    staffmemberlistrecyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    staffmemberlistrecyler.setHasFixedSize(true);
                    staffmemberlistrecyler.setAdapter(staffMemberAdapter);
                    etsearchmemberId.setVisibility(View.VISIBLE);
                    staffMemberAdapter.notifyDataSetChanged();
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