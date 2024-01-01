package com.example.societyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class StaffMemberAdapter extends RecyclerView.Adapter<StaffMemberAdapter.MyStaffListView> {
    Context context;
    String number;

    public  static List<StaffMemberViewModel>staffMemberViewModels=new ArrayList<>();

    public StaffMemberAdapter(Context context, List<StaffMemberViewModel> staffMemberViewModels) {
        this.context=context;
        this.staffMemberViewModels=staffMemberViewModels;


    }

    public void filterdList(List<StaffMemberViewModel> staffSearchModels) {
        staffMemberViewModels=staffSearchModels;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public MyStaffListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_staff_members_,null,false);

        return new MyStaffListView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyStaffListView holder, int position) {
        final StaffMemberViewModel staffMemberViewModel=staffMemberViewModels.get(position);
        String image=staffMemberViewModel.staffimageurl;
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+image).into(holder.userimagestaffId);
        holder.tvstaffmemberNameId.setText(staffMemberViewModel.staffusername);
        holder.tvstaffmemberflatId.setText(staffMemberViewModel.staffflatno);
        holder.tvstaffmembernumberId.setText(staffMemberViewModel.staffmobileno);
        holder.tvmemberServiceNameId.setText(staffMemberViewModel.staffservicename);
        holder.rlmakecallId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number=holder.tvstaffmembernumberId.getText().toString().trim();

                Intent call=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Uri.encode(number)));
               context.startActivity(call);

            }
        });
    }

    @Override
    public int getItemCount() {
        return staffMemberViewModels.size();
    }

    public class MyStaffListView extends RecyclerView.ViewHolder {
        CircleImageView userimagestaffId;
        TextView tvstaffmemberNameId,tvstaffmemberflatId,tvstaffmembernumberId,tvmemberServiceNameId;
        TextView staffmembercallId;
        RelativeLayout rlmakecallId;
        public MyStaffListView(@NonNull View itemView) {
            super(itemView);
            userimagestaffId=itemView.findViewById(R.id.userimagestaffId);
            tvstaffmemberNameId=itemView.findViewById(R.id.tvstaffmemberNameId);
            tvstaffmemberflatId=itemView.findViewById(R.id.tvstaffmemberflatId);
            tvstaffmembernumberId=itemView.findViewById(R.id.tvstaffmembernumberId);
            tvmemberServiceNameId=itemView.findViewById(R.id.tvmemberServiceNameId);
            staffmembercallId=itemView.findViewById(R.id.staffmembercallId);
            rlmakecallId=itemView.findViewById(R.id.rlmakecallId);
        }
    }
}