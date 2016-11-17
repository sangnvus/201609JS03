package com.favn.ambulance.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.ambulance.direction.DirectionFinder;
import com.favn.ambulance.direction.DirectionFinderListener;
import com.favn.ambulance.direction.Leg;
import com.favn.ambulance.networkUtil.NetworkStatus;
import com.favn.ambulance.locationUtil.LocationChangeListener;
import com.favn.ambulance.locationUtil.LocationFinder;
import com.favn.ambulance.locationUtil.LocationStatus;
import com.favn.ambulance.models.Commons.Constants;
import com.favn.mikey.ambulance.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;

public class Task extends AppCompatActivity implements OnMapReadyCallback,
        LocationChangeListener, DirectionFinderListener, View.OnClickListener {
    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private DirectionFinder directionFinder;
    private LatLng destinationLanLng;
    private Marker destinationMarkers;
    private Polyline polylinePaths;

    private LocationFinder locationFinder;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    IntentFilter intentFilter;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private CardView cvDirection;
    private LinearLayout llLoadingStatus;
    private LinearLayout llDirectionResult;
    private Button btnClearDirection;
    private TextView tvDistance;
    private TextView tvDuration;

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

        // showing caller location
        destinationLanLng = new LatLng(21.011371, 105.525721
        );

    }

    private void createUI() {
        cvDirection = (CardView) findViewById(R.id.cardview_direction);
        llLoadingStatus = (LinearLayout) findViewById(R.id.layout_loading_status);
        llDirectionResult = (LinearLayout) findViewById(R.id.layout_direction_result);
        btnClearDirection = (Button) findViewById(R.id.button_clear_direction);
        btnClearDirection.setOnClickListener(this);
        tvDistance = (TextView) findViewById(R.id.textview_distance);
        tvDuration = (TextView) findViewById(R.id.textview_duration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.direction:
                sendDirectionRequest();
                break;
            case R.id.report_problem:
                reportProblem();
                break;
            case R.id.finish_task:
                break;
        }
        return true;
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

        createMarker(destinationLanLng, "caller address");
        zoomDestinationLocation();
    }

    // Broadcast for Connectivity status
    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only work when click on off button
            if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                isLocationEnable = LocationStatus.checkLocationProvider(context);
                Log.d("location", isLocationEnable + "gps");

            } else if (intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                if (!isNetworkEnable) {
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
        locationFinder.connectGoogleApiClient();
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

    // Direction

    private void createMarker(LatLng latLng, String healthFacilityName) {

        MarkerOptions markerOptions = new MarkerOptions()
                .title(healthFacilityName)
                .position(latLng);

        destinationMarkers = mGoogleMap.addMarker(markerOptions);
        destinationMarkers.showInfoWindow();
    }

    private void goToLocationZoom(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void zoomCurrentLocation() {
        if (mCurrentLocation != null) {
            goToLocationZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
                    .getLongitude()), Constants.ZOOM_LEVEL_15);
        }
    }

    private void zoomDestinationLocation() {
        if (destinationLanLng != null) {
            goToLocationZoom(destinationLanLng, Constants.ZOOM_LEVEL_15);
        }
    }

    private void reportProblem() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:115"));

        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    // TODO notify finish task to server
    private void finishTask() {

    }

    private void sendDirectionRequest() {
        buildNetworkSetting();
        if(isNetworkEnable) {
            if (mCurrentLocation != null && destinationLanLng != null) {
                try {
                    directionFinder = new DirectionFinder(this, mCurrentLocation.getLatitude() + "," +
                            mCurrentLocation
                                    .getLongitude(), destinationLanLng.latitude + "," + destinationLanLng
                            .longitude);
                    directionFinder.execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            manageDirectionUI(true);
            llLoadingStatus.setVisibility(View.VISIBLE);
            llDirectionResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFinderSuccess(Leg leg) {
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true)
                .color(getResources().getColor(R.color.colorGpsFixed))
                .width(10);

        for (int i = 0; i < leg.getListLatLng().size(); i++)
            polylineOptions.add(leg.getListLatLng().get(i));

        polylinePaths = mGoogleMap.addPolyline(polylineOptions);
        updateDirectionUI(leg.getDistance().getText(), leg.getDuration().getText());
    }

    private void manageDirectionUI(boolean isShow) {
        final View view = findViewById(R.id.cardview_direction);
        if (isShow) {
            view.setVisibility(View.VISIBLE);
            view.animate()
                    .translationY(-8)
                    .alpha(1f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
        } else {
            view.animate()
                    .translationY(8)
                    .alpha(0.0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    private void updateDirectionUI(String distance, String duration) {
        llLoadingStatus.setVisibility(View.GONE);
        llDirectionResult.setVisibility(View.VISIBLE);
        tvDistance.setText(distance);
        tvDuration.setText(duration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_clear_direction:
                clearDirection();
                break;
        }
    }

    private void clearDirection() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        directionFinder.stop();
        manageDirectionUI(false);
        if (polylinePaths != null) {
            polylinePaths.remove();
        }
    }

}
