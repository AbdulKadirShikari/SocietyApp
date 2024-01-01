package com.example.societyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionFormActivity extends AppCompatActivity {
    RelativeLayout bottomrelDiscussId;
    RecyclerView allpostDiscusSHowId;
    List<DiscussFormViewModel>discussFormViewModels=new ArrayList<>();
    private DiscussFormAdapter discussFormAdapter;
    CircleImageView staffuserpostId;
    TextView StaffnameDisplayId,chosephotosendId;
    Button sharepostId;
    ImageView choseshowimageId,ivarrowId;
    Bitmap uploadedimage;
    String posttext,userunique_id,sid;
    TextInputEditText etPostdiscussBoxId;
    SharedPreferences userID_Shared;
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_form);
        Toolbar toolbar = findViewById(R.id.discussontoolbar);
        toolbar.setTitle("Discussion Forum");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        allpostDiscusSHowId=(RecyclerView)findViewById(R.id.allpostDiscusSHowId);

        /*bottomrelDiscussId=(RelativeLayout)findViewById(R.id.bottomrelDiscussId);*/

        getAllPostDiscussform();
        /*bottomrel




        DiscussId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postcommentDiscuss=new Intent(DiscussionFormActivity.this,PostComDiscussActivty.class);
                startActivity(postcommentDiscuss);
            }
        });*/

        staffuserpostId=(CircleImageView)findViewById(R.id.staffuserpostId);
        StaffnameDisplayId=(TextView)findViewById(R.id.StaffnameDisplayId);
        sharepostId=(Button)findViewById(R.id.sharepostId);
        etPostdiscussBoxId=(TextInputEditText) findViewById(R.id.etPostdiscussBoxId);
        chosephotosendId=findViewById(R.id.chosephotosendId);
        choseshowimageId=(ImageView)findViewById(R.id.choseshowimageId);
        ivarrowId=(ImageView)findViewById(R.id.ivarrowId);
        slidingUpPanelLayout=(SlidingUpPanelLayout) findViewById(R.id.slidingpanel);

        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise_arrow);
                ivarrowId.startAnimation(animation);
            }
        });
        userID_Shared=getSharedPreferences("profileinformation",MODE_PRIVATE);
        sid=userID_Shared.getString("loginsid","loginsid");
        userunique_id=userID_Shared.getString("loginId","loginId");
        Log.e("userunique_id","userunique_id ::"+userunique_id);

        String username=userID_Shared.getString("loginusername","loginusername");
        String userimage=userID_Shared.getString("loginimage_url", "loginimageurl");

        StaffnameDisplayId.setText(username);
       // staffuserpostId.setImageURI(Uri.parse(userimage));
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+userimage).into(staffuserpostId);


        Log.e("text","text"+username);

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

                    Toast.makeText(DiscussionFormActivity.this,"Required To write Comment",Toast.LENGTH_LONG).show();
                }else {
                    sendyourpostTOMembers();
                }
            }
        });
    }

    public void getAllPostDiscussform() {
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading please wait...", true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://placementsadda.com/Society/getAllPost", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Status", "Status ::" + response);

                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    int discusstatus = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    JSONArray logindata = jsonObject.getJSONArray("data");
                    if (discusstatus == 200) {

                        for (int i = 0; i < logindata.length(); i++) {
                            JSONObject discussSuccessData = logindata.getJSONObject(i);
                            String post_id = discussSuccessData.getString("post_id");
                            String user_id = discussSuccessData.getString("user_id");
                            String post_description = discussSuccessData.getString("post_description");
                           String post_image = discussSuccessData.getString("post_image");
                            String created_date = discussSuccessData.getString("created_date");
                            String user_our_image = discussSuccessData.getString("image");
                            String user_member_name = discussSuccessData.getString("member_name");
                           // int likes_count = discussSuccessData.getInt("likes_count");
                            DiscussFormViewModel discussFormViewModel = new DiscussFormViewModel();
                            discussFormViewModel.post_id = post_id;
                            discussFormViewModel.user_id = user_id;
                          //  discussFormViewModel.likes_count = likes_count;
                            discussFormViewModel.post_description = post_description;
                            discussFormViewModel.post_image = post_image;
                            discussFormViewModel.created_date = created_date;
                            discussFormViewModel.user_image = user_our_image;
                            discussFormViewModel.user_member_name = user_member_name;
                            discussFormViewModels.add(discussFormViewModel);
                           // Log.e("showImage","showimage"+likes_count);

                        }
                        discussFormAdapter = new DiscussFormAdapter(DiscussionFormActivity.this, discussFormViewModels);
                        allpostDiscusSHowId.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        allpostDiscusSHowId.setHasFixedSize(true);
                        allpostDiscusSHowId.setAdapter(discussFormAdapter);
                      //  dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               dialog.dismiss();
                Log.e("error", "error::" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("sid", sid);
                Log.e("sidparam", "param::" + params);
                return params;
            }


        };

        requestQueue.add(stringRequest);
        }


    public void updateListPostLike(int position) {
      DiscussFormViewModel discussFormViewModel=  discussFormViewModels.get(position);
        discussFormViewModel.likes_count=   discussFormViewModel.likes_count+1;
        discussFormViewModels.set(position,discussFormViewModel);
        discussFormAdapter.notifyDataSetChanged();
    }

    public void updateListPostUnLike(int position) {
        DiscussFormViewModel discussFormViewModel=discussFormViewModels.get(position);
        discussFormViewModel.likes_count=discussFormViewModel.likes_count-1;
        discussFormViewModels.set(position,discussFormViewModel);
        discussFormAdapter.notifyDataSetChanged();
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
                       // Log.e("postingsuccess","postingsuccess ::"+response);

                        try {
                            JSONObject jsonObject=new JSONObject(sresponse);
                            Log.e("postingsuccess","postingsuccess ::"+sresponse);
                            dialog.dismiss();
                            /*int succesfullpoststatus=jsonObject.getInt("status");
                            String succesfullpostmessage=jsonObject.getString("message");
                            String succesfullpostdata=jsonObject.getString("data");
                            if (succesfullpoststatus==200){

                                Intent discusform=new Intent(DiscussionFormActivity.this, DiscussionFormActivity.class);
                                startActivity(discusform);
                            }*/
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
                params.put("sid",sid);
                params.put("post_description",posttext);
                Log.e("paramfornewpost","param::"+params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("post_image", new DataPart(imagename + ".png", getFileDataFromDrawable(uploadedimage)));
                Log.e("imagefornewpost","image::"+uploadedimage);
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
