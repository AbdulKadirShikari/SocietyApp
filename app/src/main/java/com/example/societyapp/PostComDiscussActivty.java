package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.societyapp.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostComDiscussActivty extends AppCompatActivity {
    CircleImageView staffuserpostId;
    TextView StaffnameDisplayId,chosephotosendId;
    Button sharepostId;
    ImageView choseshowimageId;
    Bitmap uploadedimage;
    String posttext,userunique_id;
    TextInputEditText etPostdiscussBoxId;
    SharedPreferences userID_Shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_com_discuss);
        Toolbar toolbar = findViewById(R.id.postdiscussontoolbar);
        toolbar.setTitle("Send Yout Post");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        staffuserpostId=(CircleImageView)findViewById(R.id.staffuserpostId);
        StaffnameDisplayId=(TextView)findViewById(R.id.StaffnameDisplayId);
        sharepostId=(Button)findViewById(R.id.sharepostId);
        etPostdiscussBoxId=(TextInputEditText) findViewById(R.id.etPostdiscussBoxId);
        chosephotosendId=findViewById(R.id.chosephotosendId);
        choseshowimageId=(ImageView)findViewById(R.id.choseshowimageId);


        userID_Shared=getSharedPreferences("profileinformation",MODE_PRIVATE);
        userunique_id=userID_Shared.getString("loginId","loginId");
        Log.e("userunique_id","userunique_id ::"+userunique_id);

        String username=userID_Shared.getString("loginusername","loginusername");
        String userimage=userID_Shared.getString("loginimage_url", "loginimageurl");

        StaffnameDisplayId.setText(username);
        Picasso.get().load("http://placementsadda.com/Society/"+userimage).into(staffuserpostId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        chosephotosendId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        sharepostId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posttext=etPostdiscussBoxId.getText().toString();
                if (posttext==null || posttext.isEmpty()){
                    Toast.makeText(PostComDiscussActivty.this,"Required To write Comment",Toast.LENGTH_LONG).show();
                }else {
                    sendyourpostTOMembers();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                uploadedimage= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                choseshowimageId.setImageBitmap(uploadedimage);

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

    private void sendyourpostTOMembers() {
        final ProgressDialog dialog=ProgressDialog.show(this,"","Loading please wait...",true);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://placementsadda.com/Society/new_post",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String sresponse= new String(response.data);
                        try {
                            dialog.dismiss();
                            Log.e("postingyours","postingyours ::"+response);
                            JSONObject jsonObject=new JSONObject(sresponse);
                            int succesfullpoststatus=jsonObject.getInt("status");
                            String succesfullpostmessage=jsonObject.getString("message");
                            String succesfullpostdata=jsonObject.getString("data");
                            if (succesfullpoststatus==200){
                                dialog.dismiss();
                                Intent discusform=new Intent(PostComDiscussActivty.this, DiscussionFormActivity.class);
                                startActivity(discusform);
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
                params.put("user_id",userunique_id);
                params.put("post_description",posttext);
                Log.e("param","param::"+params);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("post_image", new DataPart(imagename + ".png", getFileDataFromDrawable(uploadedimage)));
                Log.e("image","image::"+uploadedimage);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
