package com.example.projectsoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard()).commit();
        }
        if(getIntent().getStringExtra("Intent") != null) {
            if (getIntent().getStringExtra("Intent").equals("LoginClass") ||
                    getIntent().getStringExtra("Intent").equals("RegisterClass") ||
                    getIntent().getStringExtra("Intent").equals("UploadPhoto") ||
                    getIntent().getStringExtra("Intent").equals("RecordsClass")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
            } else if (getIntent().getStringExtra("Intent").equals("DogActivity")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListOfDogs()).commit();
            } else if (getIntent().getStringExtra("Intent").equals("DogActivityOM")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Services()).commit();
            }else if (getIntent().getStringExtra("Intent").equals("AddNoteActivity")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard()).commit();
            }
        }
    }

    private void initialize(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_account :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                break;
            case R.id.nav_listOfDogs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListOfDogs()).commit();
                break;
            case R.id.nav_search :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Search()).commit();
                break;
            case R.id.nav_dashboard :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard()).commit();
                break;
            case R.id.nav_services :
                getIntent().putExtra("Services","services");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Services()).commit();
                break;
            case R.id.nav_shelter_help :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Call()).commit();
                break;
            case R.id.nav_about :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUs()).commit();
                break;
            case R.id.nav_share :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Share()).commit();
                break;
            case R.id.nav_exit :
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
