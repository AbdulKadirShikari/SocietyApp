package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btGoLoginId;
    SharedPreferences predetail;
    TextView tvSignUpId,tvForgotpasswordId;
    Intent preintent;
    Boolean connected=false;
    String useremail,userpwd;
    TextInputEditText etMobileLoginId,etPasswordLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        etMobileLoginId=(TextInputEditText) findViewById(R.id.etMobileLoginId);
        etPasswordLoginId=(TextInputEditText) findViewById(R.id.etPasswordLoginId);
        tvForgotpasswordId=(TextView)findViewById(R.id.tvForgotpasswordId);
        btGoLoginId=(Button)findViewById(R.id.btGoLoginId);
        tvSignUpId=(TextView)findViewById(R.id.tvSignUpId);



        predetail=getSharedPreferences("profileinformation",MODE_PRIVATE);
        preintent=new Intent(MainActivity.this,Society_Dashboard_activity.class);
        if (predetail.contains("loginemail")&&predetail.contains("loginpassword")){
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
                startActivity(preintent);
                finish();
            }else
                connected = false;

        }

        tvForgotpasswordId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent forgorpassword = new Intent(MainActivity.this, Forgetpassword.class);
                    startActivity(forgorpassword);

            }
        });

        btGoLoginId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useremail=etMobileLoginId.getText().toString().trim();
                userpwd=etPasswordLoginId.getText().toString().trim();
                if (TextUtils.isEmpty(etMobileLoginId.getText())||!(Patterns.EMAIL_ADDRESS.matcher(etMobileLoginId.getText()).matches())){
                    etMobileLoginId.requestFocus();
                    etMobileLoginId.setError("please enter valid email id");
                }else if (userpwd==null || userpwd.isEmpty()){
                    Toast.makeText(MainActivity.this,"Required To fill password",Toast.LENGTH_LONG).show();
                }else
                    RequestcheckValidLogin();
                }
        });

        tvSignUpId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registeration=new Intent(MainActivity.this,Society_Registration.class);
                startActivity(registeration);
            }
        });
    }


    public void RequestcheckValidLogin(){
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(1, "http://placementsadda.com/Society/member_login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("Status","Status ::"+response);
                    dialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    int loginstatus=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    String logindata=jsonObject.getString("data");
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    if (loginstatus==200){
                        JSONObject loginjson=new JSONObject(logindata);
                            String loginmId = loginjson.getString("mid");
                            String loginsid = loginjson.getString("sid");
                            String loginusername = loginjson.getString("member_name");
                            String loginmobileno = loginjson.getString("contact");
                            String loginflatno = loginjson.getString("flatno");
                            String loginaddress = loginjson.getString("address");
                            String loginemail = loginjson.getString("email");
                            String loginadhar_no = loginjson.getString("adhar_card");
                            String loginpassword = loginjson.getString("password");
                            String loginimageurl = loginjson.getString("image");
                            SharedPreferences sharedPreferences = getSharedPreferences("profileinformation", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loginId", loginmId);
                            editor.putString("loginsid", loginsid);
                            editor.putString("loginusername", loginusername);
                            editor.putString("loginmobileno", loginmobileno);
                            editor.putString("loginflatno", loginflatno);
                            editor.putString("loginaddress", loginaddress);
                            editor.putString("loginemail", loginemail);
                            editor.putString("loginadhar_no", loginadhar_no);
                            editor.putString("loginpassword", loginpassword);
                            editor.putString("loginimage_url", loginimageurl);
                            editor.commit();
                            Intent dashboard=new Intent(MainActivity.this,Society_Dashboard_activity.class);
                            startActivity(dashboard);
                            finish();
                        }

                }
                catch (JSONException e) {
                    Log.e("loginerror","loginerror"+e);
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
                params.put("email",useremail);
                params.put("password",userpwd);
                Log.e("param","param::"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
