package com.favn.firstaid.activites;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.favn.firstaid.R;
import com.favn.firstaid.fragments.AboutFragment;
import com.favn.firstaid.fragments.EmergencyFragment;
import com.favn.firstaid.fragments.LearningFragment;
import com.favn.firstaid.fragments.MoreFragment;
import com.favn.firstaid.fragments.NotificationFragment;
import com.favn.firstaid.fragments.SettingFragment;
import com.favn.firstaid.utils.NetworkStatus;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String folderName = "images";
    private String filePath = "FileStorage";
    File myInternalFile;
    String timeSharedPreferences = "updated_time";
    String updatedTimeKey = "updated_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initial Emergency Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new EmergencyFragment()).commit();
        setTitle("Khẩn cấp");

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

        // Check for update content

        //TODO consider set time for check update(days)
//        SharedPreferences sharedpreferences = getSharedPreferences(timeSharedPreferences, this
//                .MODE_PRIVATE);
//
//        String lastUpdateTime = sharedpreferences.getString(updatedTimeKey, "");
        // if last update time null save current date then check for update
//        if(lastUpdateTime.equals("")) {
//            Date currentDate = new Date();
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString(updatedTimeKey, currentDate.toString());
//        }
//
//
//        long diff = currentDate.getTime() - d.getTime();
//        Log.d("time_test", currentDate + "");
//        Log.d("time_test", TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "");
//
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString(updatedTimeKey, currentDate.toString());
        if (NetworkStatus.checkNetworkEnable(this)) {
//            UpdateChecking updateChecking = new UpdateChecking();
//            updateChecking.execute("http://favn.rtsvietnam.com/injury");

        }
        createInternalFile();


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
            setTitle("Khẩn cấp");
        } else if (id == R.id.nav_learning) {
            fragmentClass = LearningFragment.class;
            setTitle("Tìm hiểu");
        } else if (id == R.id.nav_more) {
            fragmentClass = MoreFragment.class;
            setTitle("Thêm");
        } else if (id == R.id.nav_notification) {
            fragmentClass = NotificationFragment.class;
            setTitle("Thông báo");
        } else if (id == R.id.nav_setting) {
            fragmentClass = SettingFragment.class;
            setTitle("Cài đặt");
        } else if (id == R.id.nav_infor) {
            fragmentClass = AboutFragment.class;
            setTitle("Thông tin phần mềm");
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

    private void createInternalFile() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(filePath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory , folderName);
    }
}
