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
import android.util.Log;
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Class fragmentClass = null;
        if (id == R.id.nav_emergency) {
            fragmentClass = EmergencyFragment.class;
        } else if (id == R.id.nav_learning) {
            fragmentClass = LearningFragment.class;
        } else if (id == R.id.nav_testing) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_qa) {
            fragmentClass = QAFragment.class;
        } else if (id == R.id.nav_noti) {
            fragmentClass = NotificationFragment.class;
        } else if (id == R.id.nav_setting) {
            fragmentClass = SettingFragment.class;
        } else if (id == R.id.nav_history) {
            fragmentClass = HistoryFragment.class;
        } else if (id == R.id.nav_infor) {
            fragmentClass = AboutFragment.class;
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
