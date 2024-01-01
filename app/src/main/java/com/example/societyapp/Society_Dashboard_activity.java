package com.example.societyapp;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Society_Dashboard_activity extends AppCompatActivity {
    CircleImageView userimageDrawerId;
    private AppBarConfiguration mAppBarConfiguration;
    TextView drawerUsername;
    SharedPreferences predetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society__dashboard_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View mview=navigationView.inflateHeaderView(R.layout.nav_header_society__dashboard_activity);

        predetail=getSharedPreferences("profileinformation",MODE_PRIVATE);


        String username=predetail.getString("loginusername","loginusername");
        String userimage=predetail.getString("loginimage_url", "loginimageurl");

        userimageDrawerId=(CircleImageView)mview.findViewById(R.id.userimageDrawerId);
        drawerUsername=(TextView)mview.findViewById(R.id.drawerUsername);

        drawerUsername.setText(username);
        //userimageDrawerId.setImageURI(Uri.parse(userimage));
       Picasso.get().load("http://placementsadda.com/Society/uploads/"+userimage).into(userimageDrawerId);

        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_logout,
                R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                R.drawable.ic_menu,R.string.menu_home,R.string.menu_profile,R.string.menu_logout){
        };
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer_toggle);*/


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.society__dashboard_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_notify:
                Intent notification=new Intent(Society_Dashboard_activity.this,ActivityNotification.class);
                startActivity(notification);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
