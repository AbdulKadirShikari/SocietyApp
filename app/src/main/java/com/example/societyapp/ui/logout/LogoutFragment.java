package com.example.societyapp.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.societyapp.MainActivity;
import com.example.societyapp.R;

public class LogoutFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("profileinformation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        sharedPreferences.edit().remove("profileinformation");
        editor.clear();
        editor.commit();
        Intent intent=new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        return root;
    }
}