package com.example.societyapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

class DiscussFormAdapter extends RecyclerView.Adapter<DiscussFormAdapter.MyDiscussForm> {
    Context context;
    List<DiscussFormViewModel>discussFormViewModels=new ArrayList<>();
    Dialog comentDialog;
    ImageView sendtextpostSuccessId,commentcloseImageId;
    TextView commentUserNameDisplayId;
    CircleImageView commentpostuserimageId;
    EditText etTextPostDisplayId;
    RecyclerView recyclerALlcomentId;
    String posttextcomments,userunique_id,post_id,usercommntpost;
    SharedPreferences userID_Shared;
    List<GetAllCommentsViewModel>getAllCommentsViewModels=new ArrayList<>();
    String like_count;
    private String userproimage,userName;


    public DiscussFormAdapter(Context context, List<DiscussFormViewModel> discussFormViewModels) {
        this.context=context;
        this.discussFormViewModels=discussFormViewModels;

    }

    @NonNull
    @Override
    public MyDiscussForm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.all_user_discuss_post_show,null,false);
        return new MyDiscussForm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDiscussForm holder, final int position) {
        final DiscussFormViewModel discussFormViewModel=discussFormViewModels.get(position);
//        holder.allnameDisplayId.setText("UserName");

        userID_Shared=context.getSharedPreferences("profileinformation",MODE_PRIVATE);
        userunique_id=userID_Shared.getString("loginId","loginId");
        userName=userID_Shared.getString("loginusername","loginusername");
        userproimage=userID_Shared.getString("loginimage_url", "loginimage_url");

        holder.tvuserpostedtextShowId.setText(discussFormViewModel.post_description);
        holder.alltimeofPostDisplayId.setText(discussFormViewModel.created_date);
        holder.allnameDisplayId.setText(discussFormViewModel.user_member_name);
       holder.likecount_tv.setText(""+discussFormViewModel.likes_count);
        post_id=discussFormViewModel.post_id;

        Log.e("user","user"+discussFormViewModel.user_member_name);
        String user_name_image=discussFormViewModel.user_image;
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+user_name_image).into(holder.alluserpostedId);

        String postedimage=discussFormViewModel.post_image;
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+postedimage).into(holder.usercommntpost);

        Log.e("post","post"+postedimage);
        comentDialog=new Dialog(context);

        holder.tvCommentUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentDialogBox();
            }
        });

        holder.tvLIkeUserId.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                post_id=discussFormViewModel.post_id;
                if (checked) {
//                    tvLIkeUserId.setBtnFillColor(Color.RED);
                    like_count="1";

                    LikePost(position);

                }else {
                    //tvLIkeUserId.setBtnColor(Color.GRAY);
                    like_count="0";
                    UnLikePost(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return discussFormViewModels.size();
    }

    public class MyDiscussForm extends RecyclerView.ViewHolder {
        CircleImageView alluserpostedId;
        TextView allnameDisplayId,alltimeofPostDisplayId,tvuserpostedtextShowId,tvCommentUserId,likecount_tv;
        ShineButton tvLIkeUserId;
        ImageView usercommntpost;

        public MyDiscussForm(@NonNull View itemView) {
            super(itemView);
            alluserpostedId=itemView.findViewById(R.id.alluserpostedId);
            allnameDisplayId=itemView.findViewById(R.id.allnameDisplayId);
            tvCommentUserId=itemView.findViewById(R.id.tvCommentUserId);
            tvLIkeUserId=itemView.findViewById(R.id.tvLIkeUserId);
            alltimeofPostDisplayId=itemView.findViewById(R.id.alltimeofPostDisplayId);
            tvuserpostedtextShowId=itemView.findViewById(R.id.tvuserpostedtextShowId);
            usercommntpost=itemView.findViewById(R.id.ivImgesAllPostedId);
            likecount_tv=itemView.findViewById(R.id.likecount_tv);
            /*ShineButton shineButtonJava = new ShineButton(context);

            shineButtonJava.setShapeResource(R.raw.like);
            shineButtonJava.setAllowRandomColor(true);
            shineButtonJava.setShineSize(100);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            shineButtonJava.setLayoutParams(layoutParams);*/

        }
    }

    public void showCommentDialogBox(){
        comentDialog.setContentView(R.layout.dialog_comment_post);
        recyclerALlcomentId=comentDialog.findViewById(R.id.recyclerALlcomentId);
        sendtextpostSuccessId=comentDialog.findViewById(R.id.sendtextpostSuccessId);
        commentUserNameDisplayId=comentDialog.findViewById(R.id.commentUserNameDisplayId);
        etTextPostDisplayId=comentDialog.findViewById(R.id.etTextPostDisplayId);
        commentcloseImageId=comentDialog.findViewById(R.id.commentcloseImageId);
        commentpostuserimageId=comentDialog.findViewById(R.id.commentpostuserimageId);
        commentcloseImageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comentDialog.dismiss();
            }
        });
        GetAllcommentsmemberData();
        //commentpostuserimageId.setImageURI(Uri.parse(userproimage));
       Picasso.get().load("http://placementsadda.com/Society/uploads/"+userproimage).into(commentpostuserimageId);
        commentUserNameDisplayId.setText(userName);

        sendtextpostSuccessId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posttextcomments=etTextPostDisplayId.getText().toString();
                etTextPostDisplayId.setText("");
                if (posttextcomments==null){
                    Toast.makeText(context,"Write Comment",Toast.LENGTH_LONG).show();
                }else {
                    sendYourCommenttoall();
                }
            }
        });

        comentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        comentDialog.show();
    }


    public void sendYourCommenttoall() {
        final ProgressDialog dialog = ProgressDialog.show(context, "", "Loading please wait...", true);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(1, "http://placementsadda.com/Society/post_comments", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("commentspost", "commentspost ::" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    dialog.dismiss();
                    int commenttatus = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    String logindata = jsonObject.getString("data");
                    if (commenttatus == 200) {
                        GetAllcommentsmemberData();
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
                HashMap<String,String> params=new HashMap<>();
                params.put("post_id",post_id);
                params.put("user_id",userunique_id);
                params.put("comments_text",posttextcomments);
                Log.e("paramcomments","param::"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void LikePost(final int position) {
        final ProgressDialog dialog = ProgressDialog.show(context, "", "Loading please wait...", true);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(1, "http://placementsadda.com/Society/post_likes", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.e("likepost", "likepost ::" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    int likesuccess=jsonObject.getInt("status");
                    if (likesuccess==200) {
                        ((DiscussionFormActivity)context).updateListPostLike(position);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("catcherror", "catcherror::" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("error", "error::" + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("post_id", post_id);
                params.put("user_id",userunique_id);
                params.put("like_dislike",like_count);
                Log.e("paramlikes", "param::" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void UnLikePost(final int position) {
        final ProgressDialog dialog = ProgressDialog.show(context, "", "Loading please wait...", true);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(1, "http://placementsadda.com/Society/post_likes", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.e("unlikepost", "unlikepost ::" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    int unlikesuccess=jsonObject.getInt("status");
                    if (unlikesuccess==200) {
                        ((DiscussionFormActivity)context).updateListPostUnLike(position);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("catcherror", "catcherror::" + e);
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
                params.put("post_id", post_id);
                params.put("user_id",userunique_id);
                params.put("like_dislike",like_count);
                Log.e("param", "param::" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void GetAllcommentsmemberData(){
        final ProgressDialog dialog=ProgressDialog.show(context,"","Loading please wait...",true);
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(1, "http://placementsadda.com/Society/getCommentsByPostId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("commentspost","commentspost ::"+response);
                    dialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    int commenttatus=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    JSONArray commentdata=jsonObject.getJSONArray("data");
                    if (commenttatus==200){
                        getAllCommentsViewModels.clear();
                        if (commentdata.equals(false)){

                        }else {
                            for (int i = 0; i < commentdata.length(); i++) {
                                JSONObject discussSuccessData = commentdata.getJSONObject(i);
                                String member_name = discussSuccessData.getString("member_name");
                                String member_image = discussSuccessData.getString("image");
                                  Log.e("verify","vrfy"+member_image);
                                String post_id = discussSuccessData.getString("post_id");
                                String user_id = discussSuccessData.getString("user_id");
                                String comments_text = discussSuccessData.getString("comments_text");
                                String comments_id = discussSuccessData.getString("comments_id");
                                String created_date = discussSuccessData.getString("created_date");
                                GetAllCommentsViewModel getAllCommentsViewModel = new GetAllCommentsViewModel();
                               getAllCommentsViewModel.member_name = member_name;
                                getAllCommentsViewModel.member_image = member_image;
                                getAllCommentsViewModel.post_id = post_id;
                                getAllCommentsViewModel.user_id = user_id;
                                getAllCommentsViewModel.cocmmentusercomments = comments_text;
                                Log.e("msssg","mssg"+comments_text);


                                getAllCommentsViewModel.comment_id = comments_id;
                                getAllCommentsViewModel.created_date = created_date;
                                getAllCommentsViewModels.add(getAllCommentsViewModel);

                            }
                        }
                        GetAllUsersCommentsAdapter getAllUsersComments=new GetAllUsersCommentsAdapter(context,getAllCommentsViewModels);
                        recyclerALlcomentId.setLayoutManager(new LinearLayoutManager(context));
                        recyclerALlcomentId.setHasFixedSize(true);
                        recyclerALlcomentId.setAdapter(getAllUsersComments);
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
                params.put("post_id",post_id);
                Log.e("param","param::"+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
