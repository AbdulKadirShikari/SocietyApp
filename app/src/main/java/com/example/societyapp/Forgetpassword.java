package com.example.societyapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgetpassword extends AppCompatActivity {
    TextInputEditText forgetEmailId;
    ImageView lefarrowId;
    Button resetPasswordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        lefarrowId=findViewById(R.id.lefarrowId);
        forgetEmailId=(TextInputEditText) findViewById(R.id.forgetEmailId);
        resetPasswordId=(Button)findViewById(R.id.resetPasswordId);

        lefarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(Forgetpassword.this,MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        resetPasswordId.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                String forgetEmail = forgetEmailId.getText().toString();

                if(TextUtils.isEmpty(forgetEmailId.getText())||!(Patterns.EMAIL_ADDRESS.matcher(forgetEmailId.getText()).matches())) {
                    forgetEmailId.requestFocus();
                    forgetEmailId.setError("please enter valid email id");

                }else{
                    forgetpassword();
                }
            }
        });
    }
    public void forgetpassword(){
        String url="http://placementsadda.com/EazyKaam/Login";
        String URL="http://placementsadda.com/Society/forgot_password_link";

        Log.e("mkr", "Login: " );
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Onresponse",response.toString());

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    dialog.dismiss();
                    if(jsonObject.getInt("status")==200&&jsonObject.getString("message").equals("success")){
                        Toast.makeText(Forgetpassword.this, "please check your Email", Toast.LENGTH_SHORT).show();
                        JSONObject obj =jsonObject.getJSONObject("data");

                    }
                    else{
                        dialog.dismiss();
                        Intent intent=new Intent(Forgetpassword.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(Forgetpassword.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show(); }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("Onerror",error.toString());
            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> params = new HashMap<>();
                params.put("email",forgetEmailId.getText().toString());
                Log.e("params",params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}

