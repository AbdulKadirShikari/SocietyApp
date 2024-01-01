package com.example.societyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ActivityNotification extends AppCompatActivity {
    GifImageView gifviewId;
    RecyclerView rvnotifiactionId;
    List<NotificationModel>notificationModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.notificationtoolbar);
        toolbar.setTitle("Notification");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gifviewId=(GifImageView)findViewById(R.id.gifviewId);
        rvnotifiactionId=(RecyclerView) findViewById(R.id.rvnotifiactionId);

        NotificationAdapter notificationAdapter=new NotificationAdapter(ActivityNotification.this,notificationModels);
        rvnotifiactionId.setAdapter(notificationAdapter);
    }
}
