package com.example.societyapp.ui.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.societyapp.MainActivity;
import com.example.societyapp.R;
import com.example.societyapp.Society_Registration;
import com.example.societyapp.VolleyMultipartRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class ProfileFragment extends Fragment  {
    TextInputEditText etprofileuserNameRegId,etprofileEmailRegId,etprofileMobileRegId,etprofileFlatNoRegId,
    etprofileAddressRegId,etprofileAdharCardId;
    CircleImageView profileuserimageUpdId,iv_Edit;
    SharedPreferences predetail;
    Button btupdate_ProfileId;
    LinearLayout LinearAllTextFieldId,LinearEditFieldsId,ll_Remove,ll_Gallery,ll_Camera;
    TextView tv_edit_profileId;


    Uri imageUri=null;
    PopupWindow popupWindow;
    TextView etlNameEditId,etlEmailEditId,etlmobileEditId,etlFlatnoEditId,etlAddressEditId,etlAdharEditId;
    public String usernameupd,useraddressupd,userflatnoupd,usermobupd;
    String username_Id,usermobile,userflatno,useraddress,username,useremail,useradhar,userproimage;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        etprofileuserNameRegId=root.findViewById(R.id.etprofileuserNameRegId);
        tv_edit_profileId=root.findViewById(R.id.tv_edit_profileId);
        etprofileEmailRegId=root.findViewById(R.id.etprofileEmailRegId);
        etprofileMobileRegId=root.findViewById(R.id.etprofileMobileRegId);
        etprofileFlatNoRegId=root.findViewById(R.id.etprofileFlatNoRegId);
        etprofileAddressRegId=root.findViewById(R.id.etprofileAddressRegId);
        etprofileAdharCardId=root.findViewById(R.id.etprofileAdharCardId);
        profileuserimageUpdId=root.findViewById(R.id.profileuserimageRegId);
        iv_Edit=root.findViewById(R.id.iv_Edit);
        btupdate_ProfileId=root.findViewById(R.id.btupdate_ProfileId);


        etlNameEditId=root.findViewById(R.id.etlNameEditId);
        etlEmailEditId=root.findViewById(R.id.etlEmailEditId);
        etlmobileEditId=root.findViewById(R.id.etlmobileEditId);
        etlFlatnoEditId=root.findViewById(R.id.etlFlatnoEditId);
        etlAddressEditId=root.findViewById(R.id.etlAddressEditId);
        etlAdharEditId=root.findViewById(R.id.etlAdharEditId);


        LinearAllTextFieldId=root.findViewById(R.id.LinearAllTextFieldId);
        LinearEditFieldsId=root.findViewById(R.id.LinearEditFieldsId);

        predetail=getActivity().getSharedPreferences("profileinformation",MODE_PRIVATE);


        userproimage=predetail.getString("loginimage_url", "loginimage_url");




       // profileuserimageUpdId.setImageURI(Uri.parse(userproimage));
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+userproimage).into(profileuserimageUpdId);
        Log.e("profileimg","profile"+userproimage);

        tv_edit_profileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Edit.setVisibility(View.VISIBLE);
                LinearAllTextFieldId.setVisibility(View.GONE);
                setUserDetails();
                LinearEditFieldsId.setVisibility(View.VISIBLE);


            }
        });

        setUserDetails();


        btupdate_ProfileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameupd=etprofileuserNameRegId.getText().toString();
                useraddressupd=etprofileAddressRegId.getText().toString();
                userflatnoupd=etprofileFlatNoRegId.getText().toString();
                usermobupd=etprofileMobileRegId.getText().toString();

                update_Profile();

                LinearEditFieldsId.setVisibility(View.GONE);
                   iv_Edit.setVisibility(View.GONE);
                LinearAllTextFieldId.setVisibility(View.VISIBLE);

            }
        });
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, CAMERA);
        }

        iv_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup,null);


                ll_Camera =  customView.findViewById(R.id.ll_Camera);
                ll_Gallery =  customView.findViewById(R.id.ll_Gallery);
                ll_Remove =  customView.findViewById(R.id.ll_Remove);

                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //display the popup window
                popupWindow.showAtLocation(LinearAllTextFieldId, Gravity.BOTTOM, 0, 0);

                //close the popup window on button click
                ll_Remove.setOnClickListener(this);
                ll_Gallery.setOnClickListener(this);
                ll_Camera.setOnClickListener(this);

                ll_Gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 100);
                        popupWindow.dismiss();
                    }
                });
                ll_Camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Log.e("intent", "view:-" + intent);
                        startActivityForResult(intent, 200);
                        popupWindow.dismiss();
                    }
                });
                ll_Remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        profileuserimageUpdId.setImageResource(R.drawable.ic_add_user);
                        imageUri=Uri.parse(getResources().getDrawable(R.drawable.pro).toString());
                        popupWindow.dismiss();
                    }
                });

            }

        });

        return root;
    }
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

             imageUri = data.getData();

                profileuserimageUpdId.setImageURI(imageUri);

        }
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();

                profileuserimageUpdId.setImageURI(imageUri);

        }
    }

    private void update_Profile() {
        final ProgressDialog dialog=ProgressDialog.show(getActivity(),"","Loading please wait...",true);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://placementsadda.com/Society/updateProfile",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String sresponse= new String(response.data);
                        try {
                            Log.e("StatusUpdate","StatusUpdate ::"+sresponse);
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(sresponse);
                            int loginstatus=jsonObject.getInt("status");
                            String updatedata=jsonObject.getString("data");
                            if (loginstatus==200){
                                JSONObject updateJson=new JSONObject(updatedata);
                                String loginusername = updateJson.getString("member_name");
                                String loginmobileno = updateJson.getString("contact");
                                String loginflatno = updateJson.getString("flatno");
                                String loginaddress = updateJson.getString("address");
                                String loginemail = updateJson.getString("email");
                                String loginimageurl = updateJson.getString("image");
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profileinformation", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("loginusername", loginusername);
                                editor.putString("loginmobileno", loginmobileno);
                                editor.putString("loginflatno", loginflatno);
                                editor.putString("loginaddress", loginaddress);
                                editor.putString("loginemail", loginemail);
                                editor.putString("loginimage_url", loginimageurl);
                                editor.commit();
                                setUserDetails();
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
                params.put("member_name",usernameupd);
                params.put("user_id", username_Id);
                params.put("contact",usermobupd);
                params.put("flatno",userflatnoupd);
                params.put("address",useraddressupd);
                Log.e("param","param::"+params);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                if(imageUri!=null) {
                    params.put("image", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(getActivity(),profileuserimageUpdId.getDrawable())));

                }
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    public void setUserDetails(){

        username=predetail.getString("loginusername","loginusername");
        username_Id=predetail.getString("loginId","loginId");
        usermobile=predetail.getString("loginmobileno", "loginmobileno");
        userflatno=predetail.getString("loginflatno", "loginflatno");
        useraddress=predetail.getString("loginaddress", "loginaddress");
        useremail=predetail.getString("loginemail", "loginemail");
        useradhar=predetail.getString("loginadhar_no", "loginadhar_no");
        userproimage=predetail.getString("loginimage_url", "loginimage_url");


        etlNameEditId.setText(username);
        etlmobileEditId.setText(usermobile);
        etlFlatnoEditId.setText(userflatno);
        etlAddressEditId.setText(useraddress);
        etlEmailEditId.setText(useremail);
        etlAdharEditId.setText(useradhar);

        etprofileuserNameRegId.setText(username);
        etprofileMobileRegId.setText(usermobile);
        etprofileFlatNoRegId.setText(userflatno);
        etprofileAddressRegId.setText(useraddress);
        etprofileEmailRegId.setText(useremail);
        etprofileAdharCardId.setText(useradhar);


    }




}

