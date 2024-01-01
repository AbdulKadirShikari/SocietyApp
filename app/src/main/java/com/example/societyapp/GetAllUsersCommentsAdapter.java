package com.example.societyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class GetAllUsersCommentsAdapter extends RecyclerView.Adapter<GetAllUsersCommentsAdapter.MyGetComments> {
    Context context;
    List<GetAllCommentsViewModel>getAllCommentsViewModels=new ArrayList<>();



    public GetAllUsersCommentsAdapter(Context context, List<GetAllCommentsViewModel> getAllCommentsViewModels) {
        this.context=context;
        this.getAllCommentsViewModels=getAllCommentsViewModels;
    }

    @NonNull
    @Override
    public GetAllUsersCommentsAdapter.MyGetComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_all_comments,null,false);
        return new MyGetComments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllUsersCommentsAdapter.MyGetComments holder, int position) {
        GetAllCommentsViewModel getAllCommentsViewModel=getAllCommentsViewModels.get(position);
        holder.userpostnamecommment.setText(getAllCommentsViewModel.member_name);
        holder.userposttextcommment.setText(getAllCommentsViewModel.cocmmentusercomments);
        holder.userpostingtimecommment.setText(getAllCommentsViewModel.created_date);
        String image=getAllCommentsViewModel.member_image;
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+image).into(holder.userpostimagecommment);

    }

    @Override
    public int getItemCount() {
        return getAllCommentsViewModels.size();
    }

    public class MyGetComments extends RecyclerView.ViewHolder {
        TextView userpostnamecommment,userposttextcommment,userpostingtimecommment;
        CircleImageView userpostimagecommment;
        public MyGetComments(@NonNull View itemView) {
            super(itemView);
            userpostimagecommment=itemView.findViewById(R.id.userpostimagecommment);
            userpostnamecommment=itemView.findViewById(R.id.userpostnamecommment);
            userposttextcommment=itemView.findViewById(R.id.userposttextcommment);
            userpostingtimecommment=itemView.findViewById(R.id.userpostingtimecommment);
        }
    }
}
