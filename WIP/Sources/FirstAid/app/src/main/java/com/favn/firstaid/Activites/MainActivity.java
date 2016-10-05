package com.favn.firstaid.Activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.favn.firstaid.Activites.DirectionActivity;
import com.favn.firstaid.Fragments.AboutFragment;
import com.favn.firstaid.Fragments.EmergencyFragment;
import com.favn.firstaid.Fragments.HistoryFragment;
import com.favn.firstaid.Fragments.LearningFragment;
import com.favn.firstaid.Fragments.NotificationFragment;
import com.favn.firstaid.Fragments.QAFragment;
import com.favn.firstaid.Fragments.SettingFragment;
import com.favn.firstaid.Fragments.TestFragment;
import com.favn.firstaid.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initial Emergency Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new EmergencyFragment()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_sos_calling) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:115"));

            try{
                startActivity(callIntent);
            }
            catch (android.content.ActivityNotFoundException ex){
            }
        }
        if (id == R.id.action_direction) {
            startActivity(new Intent(this, MapsActivity.class));
        }
        if(id == R.id.nav_history) {
            // TODO: check suitable method
            //android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
            //onNavigateUpFromChild(this);
            onNavigateUp();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Class fragmentClass = null;
        if (id == R.id.nav_emergency) {
            fragmentClass =  EmergencyFragment.class;
        } else if (id == R.id.nav_learning) {
            fragmentClass = LearningFragment.class;
        } else if (id == R.id.nav_testing) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_qa) {
            fragmentClass = QAFragment.class;
        } else if (id == R.id.nav_noti) {
            fragmentClass = NotificationFragment.class;
        } else if (id == R.id.nav_setting) {
            fragmentClass =SettingFragment.class;
        } else if (id == R.id.nav_history) {
            fragmentClass =HistoryFragment.class;
        } else if (id == R.id.nav_infor) {
            fragmentClass =AboutFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
