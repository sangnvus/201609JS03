package com.favn.ambulance.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.favn.ambulance.locationUtil.LocationChangeListener;
import com.favn.ambulance.locationUtil.LocationFinder;
import com.favn.ambulance.locationUtil.LocationStatus;
import com.favn.ambulance.models.Ambulance;
import com.favn.ambulance.models.Commons.Constants;
import com.favn.ambulance.models.Commons.SharedPreferencesData;
import com.favn.ambulance.networkUtil.NetworkStatus;
import com.favn.mikey.ambulance.R;
import com.google.android.gms.common.api.Status;

public class WaitingScreen extends AppCompatActivity implements LocationChangeListener {

    NotificationCompat.Builder notification;
    private static final int id = 45612;

    private Location mCurrentLocation;
    private LocationFinder locationFinder;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    IntentFilter intentFilter;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    Ambulance ambulance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        createLocationFinder();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);

        // Get ambulance info from SharedPreferences
        ambulance = SharedPreferencesData.getAmbulanceData(Constants.SPREFS_AMBULANCE_INFO_KEY);

        Log.d("ambulance_data", ambulance + "");

        createTaskDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.waiting_screen_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.sign_out:
                //build the noti
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setTicker("ticker");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Cấp cứu 115");
                notification.setContentText("Có nhiệm vụ");

                Intent intent = new Intent(this, Task.class);
                PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                //build noti and issues
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(id, notification.build());
                break;
            case R.id.application_info:
                //TODO move to about acivity
                startActivity(new Intent(this, InformationScreen.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {}
    // Broadcast for Connectivity status
    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only work when click on off button
            if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                isLocationEnable = LocationStatus.checkLocationProvider(context);

            } else if (intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                if (!isNetworkEnable) {
                    createNetworkSetting();
                }
                // Check location enable in connectivity change
                isLocationEnable = LocationStatus.checkLocationProvider(context);
            }

            locationFinder.connectGoogleApiClient();
        }
    };

    public void createLocationFinder() {
        locationFinder = new LocationFinder(this, this);
        locationFinder.buildLocationFinder();
        locationFinder.connectGoogleApiClient();
    }


    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(WaitingScreen.this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    @Override
    public void locationChangeSuccess(Location location) {
        mCurrentLocation = location;
        Log.d("location_test", location + "");

        // Need to check if ambulance != null
    }

    private void buildNetworkSetting() {
        isNetworkEnable = NetworkStatus.checkNetworkEnable(this);
        if (!isNetworkEnable) {
            createNetworkSetting();
        }
    }

    private void createNetworkSetting() {
        new AlertDialog.Builder(this)
                .setTitle("Kết nối Internet")
                .setMessage("Vào cài đặt Internet")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .create()
                .show();

    }

    private void createTaskDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Nhiệm vụ mới")
                .setMessage("Ấn 'Nhận' để nhận nhiệm vụ hoặc ấn 'Hủy' để hủy nhiệm vụ.")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelTask();
                    }
                })
                .setPositiveButton("Nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accessTask();
                    }
                })
                .create()
                .show();
    }

    private void cancelTask(){
        Toast.makeText(this, "Hủy rồi nhé !", Toast.LENGTH_LONG).show();
    }

    private void accessTask(){
        Toast.makeText(this, "Đồng ý rồi này !", Toast.LENGTH_LONG).show();
    }
}


