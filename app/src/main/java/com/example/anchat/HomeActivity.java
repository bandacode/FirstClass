package com.example.anchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anchat.ui.home.GroupFragment;
import com.example.anchat.ui.posts.PostFragment;
import com.example.anchat.ui.slideshow.SlideshowFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavController navController;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "HomeActivity";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
//
        drawer = findViewById(R.id.drawer_layout);
//        Handle nav drawer item clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_posts, R.id.nav_events, R.id.nav_notifications)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        Update action bar to reflect navigation
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        Tie Nav Graph to items in nav drawer
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNavHeader();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_user_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                mAuth.removeAuthStateListener(mAuthListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        Ensure menu items in the Nav Drawer stay in sync with the navGraph
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



public void updateNavHeader(){
    NavigationView navigationView = findViewById(R.id.nav_view);
    View headerView = navigationView.getHeaderView(0);
    TextView navUsername = headerView.findViewById(R.id.nav_user_display_name);
    TextView navEmail = headerView.findViewById(R.id.nav_user_email);
    ImageView navUserImage = headerView.findViewById(R.id.nav_user_profile);


    navUsername.setText(firebaseUser.getDisplayName());
    navEmail.setText(firebaseUser.getEmail());

    Glide.with(this)
            .load(firebaseUser.getPhotoUrl())
            .into(navUserImage);
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new GroupFragment()).commit();
        } else if (id == R.id.nav_posts){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PostFragment()).commit();


        } else if (id == R.id.nav_events){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SlideshowFragment()).commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }


    }
}
