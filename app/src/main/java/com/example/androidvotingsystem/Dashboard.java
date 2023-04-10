package com.example.androidvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Dashboard extends AppCompatActivity {

    public NavigationView navigationView;
    public Toolbar toolbar;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Sign Out function
    private void signOut() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut();
        clearData();
        clearDatabase();
    }
    //Delete User
    private void clearData() {
        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

   //Delete Data From Users
    private void clearDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("schoolId");
        reference.removeValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_group_1_item_1:
                        // Handle the group 1 item 1 action
                        Intent gotoHome = new Intent(Dashboard.this, Dashboard.class);
                        startActivity(gotoHome);
                        break;
                    case R.id.nav_group_1_item_2:
                        // Handle the group 1 item 2 action
                        Intent gotoCreate = new Intent(Dashboard.this, Createpool.class);
                        startActivity(gotoCreate);
                        break;
                    case R.id.nav_group_1_item_3:
                        // Handle the group 2 item 1 action
                        Intent gotoGroup = new Intent(Dashboard.this, Group.class);
                        startActivity(gotoGroup);
                        break;
                    case R.id.nav_group_1_item_4:
                        // Handle the group 2 item 2 action
                        Intent gotoVotes = new Intent(Dashboard.this, MyVotes.class);
                        startActivity(gotoVotes);
                        break;
                    case R.id.nav_group_1_item_5:
                        // Handle the group 2 item 2 action
                        Intent gotoProfile = new Intent(Dashboard.this, Profile.class);
                        startActivity(gotoProfile);
                        break;
                    case R.id.nav_group_2_item_6:
                        // Handle the group 2 item 2 action
                        // Call the signOut() method to log out
                        signOut();
                        Intent gotoSign_in = new Intent(Dashboard.this,MainActivity.class);
                        startActivity(gotoSign_in);
                        finish();
                        Toast.makeText(Dashboard.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                        break;
                    // Add cases for the other groups and items as needed
                }
                return true;
            }
        });
    }

}