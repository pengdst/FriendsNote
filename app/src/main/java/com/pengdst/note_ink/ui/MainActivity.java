package com.pengdst.note_ink.ui;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pengdst.note_ink.R;
import com.pengdst.note_ink.helper.Session;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;
    private String username;
    private Boolean isLogin;
    private Session session;
    private TextView headName;
    private TextView headEmail;
    private CircleImageView headPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = Session.init(getApplicationContext());
        isLogin = session.getBoolean("login");
        username = session.getString("username");
        if (!isLogin) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_note
                , R.id.nav_profile
                , R.id.nav_todo
//                , R.id.nav_about
//                , R.id.nav_gallery
//                , R.id.nav_tools
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View vNavHead = navigationView.getHeaderView(0);
        headName = vNavHead.findViewById(R.id.head_name);
        headEmail = vNavHead.findViewById(R.id.head_email);
        headPhoto = vNavHead.findViewById(R.id.head_photo);

        headName.setText(session.getString("username"));
        headEmail.setText(session.getString("email"));
        if (session.getString("photo") != null) {
//            headPhoto.setImageURI(Uri.parse(session.getString("photo")));
            Glide.with(getApplicationContext())
                    .load(session.getString("photo"))
                    .centerCrop()
                    .error(R.mipmap.ic_launcher_round)
                    .apply(new RequestOptions().override(220, 220))
                    .into(headPhoto);
        }
        Log.e("Photo", session.getString("photo"));
        if (session.getString("loca") != null) Log.e("Photo", "content://com.miui.gallery.open"+session.getString("loca"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                session.set("login", false);
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
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
