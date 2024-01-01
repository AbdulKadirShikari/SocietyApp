package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Society_Registration extends AppCompatActivity {
    TextView tvSignInId;
    TextInputEditText etuserNameRegId,etPasswordRegId,etEmailRegId,etMobileRegId,etFlatNoRegId,etAdharCardId;
    TextInputEditText etAddressRegId;
    Button btRegistrationId;
    ImageView regleftarrowId;
    CircleImageView userimageRegId;
    Bitmap uploaduserimage;
    Spinner spinnersocietyNameId;
    String usernamereg,userpwdreg,useremailreg,usermobreg,userflatnoreg,useradharreg,useraddressreg
            ,selectedSociety;
    List<String> societycorespondsid=new ArrayList<>();
    List<String> StringSocietyList=new ArrayList<>();
    int societySIdpostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society__registration);
        userimageRegId=(CircleImageView)findViewById(R.id.userimageRegId);
        spinnersocietyNameId=(Spinner) findViewById(R.id.spinnersocietyNameId);
        regleftarrowId=(ImageView)findViewById(R.id.regleftarrowId);
        etuserNameRegId=(TextInputEditText)findViewById(R.id.etuserNameRegId);
        etPasswordRegId=(TextInputEditText)findViewById(R.id.etPasswordRegId);
        etEmailRegId=(TextInputEditText)findViewById(R.id.etEmailRegId);
        etMobileRegId=(TextInputEditText)findViewById(R.id.etMobileRegId);
        etFlatNoRegId=(TextInputEditText)findViewById(R.id.etFlatNoRegId);
        etAddressRegId=(TextInputEditText)findViewById(R.id.etAddressRegId);
        etAdharCardId=(TextInputEditText)findViewById(R.id.etAdharCardId);
        btRegistrationId=(Button)findViewById(R.id.btRegistrationId);
        tvSignInId=(TextView)findViewById(R.id.tvSignInId);

        RequestShowSocietyAllName();

        spinnersocietyNameId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                {
                    selectedSociety = spinnersocietyNameId.getItemAtPosition(spinnersocietyNameId.getSelectedItemPosition()).toString();
                    societySIdpostion=i;
                    //Toast.makeText(SocietyServiceActivity.this, "serviceid"+serviceIdpostion, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        regleftarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backlobby=new Intent(Society_Registration.this,MainActivity.class);
                startActivity(backlobby);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        userimageRegId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });


        btRegistrationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamereg=etuserNameRegId.getText().toString().trim();
                userpwdreg=etPasswordRegId.getText().toString().trim();
                useremailreg=etEmailRegId.getText().toString().trim();
                usermobreg=etMobileRegId.getText().toString().trim();
                userflatnoreg=etFlatNoRegId.getText().toString().trim();
                useraddressreg=etAddressRegId.getText().toString().trim();
                useradharreg=etAdharCardId.getText().toString().trim();

                if (uploaduserimage==null){
                    Toast.makeText(Society_Registration.this,"Select an image",Toast.LENGTH_LONG).show();
                }else if (usernamereg==null || usernamereg.isEmpty()){
                    Toast.makeText(Society_Registration.this,"Required To fill your Name",Toast.LENGTH_LONG).show();
                }else if (userpwdreg==null || userpwdreg.isEmpty()){
                    Toast.makeText(Society_Registration.this,"Required To fill password",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(etEmailRegId.getText())||!(Patterns.EMAIL_ADDRESS.matcher(etEmailRegId.getText()).matches())){
                    etEmailRegId.requestFocus();
                    etEmailRegId.setError("please enter valid email id");
                }else if (usermobreg==null || usermobreg.isEmpty()){
                    Toast.makeText(Society_Registration.this,"Required To fill mobile no.",Toast.LENGTH_LONG).show();
                }else if (userflatnoreg==null || userflatnoreg.isEmpty()){
                    Toast.makeText(Society_Registration.this,"Required To fill flat-No.",Toast.LENGTH_LONG).show();
                }else if (useraddressreg==null || useraddressreg.isEmpty()) {
                    Toast.makeText(Society_Registration.this, "Required To fill Address", Toast.LENGTH_LONG).show();
                }else if (useradharreg==null || useradharreg.length()!=16){
                    Toast.makeText(Society_Registration.this,"Adhar 16 digit required",Toast.LENGTH_LONG).show();
                }else {
                     RequestcheckValidRegistration();
                }
            }
        });

        tvSignInId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alLogin=new Intent(Society_Registration.this,MainActivity.class);
                startActivity(alLogin);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                uploaduserimage= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                userimageRegId.setImageBitmap(uploaduserimage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void RequestcheckValidRegistration() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://placementsadda.com/Society/member_registration",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                       String sresponse= new String(response.data);
                        try {
                            Log.e("register","Status ::"+sresponse);
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(sresponse);
                            int loginstatus=jsonObject.getInt("status");
                            String message=jsonObject.getString("message");
                            /*JSONObject logindata=jsonObject.getJSONObject("data");*/
                            if (loginstatus==200){
                                Toast.makeText(Society_Registration.this,message,Toast.LENGTH_LONG).show();
                               /* String memberuniqueId=logindata.getString("member");*/
                                Intent login=new Intent(Society_Registration.this,MainActivity.class);
                                startActivity(login);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("catch error","error::"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.e("error","error::"+error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("member_name",usernamereg);
                params.put("contact",usermobreg);
                params.put("flatno",userflatnoreg);
                params.put("address",useraddressreg);
                params.put("email",useremailreg);
                params.put("adhar_card",useradharreg);
                params.put("password",userpwdreg);
                params.put("sid",societycorespondsid.get(societySIdpostion));
                Log.e("param","param::"+params);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(uploaduserimage)));
                Log.e("image","image::"+uploaduserimage);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void RequestShowSocietyAllName(){
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(0, "http://placementsadda.com/Society/getAllSociety", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("Status","Status ::"+response);
                    JSONObject jsonObject=new JSONObject(response);
                    dialog.dismiss();
                    int checksocietyNamestatus=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    JSONArray logindata=jsonObject.getJSONArray("data");

                    if (checksocietyNamestatus==1){
                        for (int i=0;i<logindata.length();i++) {
                            JSONObject userSuccessData = logindata.getJSONObject(i);
                            String societyId = userSuccessData.getString("sid");
                            String Getsocietyname = userSuccessData.getString("societyname");
                            StringSocietyList.add(Getsocietyname);
                            societycorespondsid.add(societyId);
                        }
                    }
                    spinnersocietyNameId.setAdapter(new ArrayAdapter<String>(Society_Registration.this, android.R.layout.simple_spinner_dropdown_item, StringSocietyList));
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
        });
        requestQueue.add(stringRequest);
    }

}
