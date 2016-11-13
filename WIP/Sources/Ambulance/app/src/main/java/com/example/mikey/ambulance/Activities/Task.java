package com.example.mikey.ambulance.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mikey.ambulance.LocationUtil.LocationChangeListener;
import com.example.mikey.ambulance.LocationUtil.LocationFinder;
import com.example.mikey.ambulance.LocationUtil.LocationStatus;
import com.example.mikey.ambulance.Models.Commons.Constants;
import com.example.mikey.ambulance.NetworkUtil.NetworkStatus;
import com.example.mikey.ambulance.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

public class Task extends AppCompatActivity implements OnMapReadyCallback, LocationChangeListener, View.OnClickListener {
    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private Marker destinationMarkers;
    private Polyline polylinePaths;

    private LocationFinder locationFinder;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    IntentFilter intentFilter;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private Button btnNavigate;
    private Button btnPickedUp;
    private Button btnFinishTask;
    private Button btnCancelTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (googleServiceAvailable()) {
            initMap();
        }
//        AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
//        dialog.setTitle("NHIỆM VỤ MỚI");
//        dialog.setMessage("Nhận nhiệm vụ mới");
//        dialog.setPositiveButton("Nhận", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // You don't have to do anything here if you just want it dismissed when clicked
//            }
//        });
//
//        dialog.setCancelable(true);
//        dialog.create().show();
//

        createLocationFinder();
        createUI();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
    }

    private void createUI() {
        btnNavigate = (Button) findViewById(R.id.button_navigate);
        btnPickedUp = (Button) findViewById(R.id.button_picked_up);
        btnFinishTask = (Button) findViewById(R.id.button_finish_task);
        btnCancelTask = (Button) findViewById(R.id.button_cancel_task);

        btnNavigate.setOnClickListener(this);
        btnPickedUp.setOnClickListener(this);
        btnFinishTask.setOnClickListener(this);
        btnCancelTask.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationFinder.disconnectGoogleApiClient();
    }

    private boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLngBounds VIETNAM = new LatLngBounds(Constants.SOUTHWEST, Constants.NORTHEAST);
        mGoogleMap.setLatLngBoundsForCameraTarget(VIETNAM);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VIETNAM.getCenter(), Constants.ZOOM_LEVEL_5));
    }

    // Broadcast for Connectivity status
    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only work when click on off button
            if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                isLocationEnable = LocationStatus.checkLocationProvider(context);
                Log.d("location", isLocationEnable + "gps");

            } else if(intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                if(!isNetworkEnable) {
                createNetworkSetting();
                }
                // Check location enable in connectivity change
                isLocationEnable = LocationStatus.checkLocationProvider(context);
                Log.d("location", isLocationEnable + "connectivity");
            }

            locationFinder.connectGoogleApiClient();
            Log.d("location", isLocationEnable + "");
        }
    };


    public void createLocationFinder() {
        locationFinder = new LocationFinder(this, this);
        locationFinder.buildLocationFinder();
    }

    // Override LocationChangeListener interface
    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(Task.this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    @Override
    public void locationChangeSuccess(Location location) {
        mCurrentLocation = location;
        Log.d("location", location + "");
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


    @Override
    public void onClick(View v) {
    switch (v.getId()) {
        case R.id.button_navigate:
            break;
        case R.id.button_picked_up:
            break;
        case R.id.button_finish_task:
            break;
        case R.id.button_cancel_task:
            break;
    }
    }
}
