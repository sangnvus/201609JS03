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
import android.os.Handler;
import android.os.ResultReceiver;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.ambulance.commons.Ambulance;
import com.favn.ambulance.commons.AmbulanceInfoSender;
import com.favn.ambulance.commons.Caller;
import com.favn.ambulance.commons.FirebaseHandle;
import com.favn.ambulance.services.CallerInformationGetter;
import com.favn.ambulance.services.CallerInformationGetterListener;
import com.favn.ambulance.services.FetchAddressIntentService;
import com.favn.ambulance.services.TaskReporter;
import com.favn.ambulance.services.direction.DirectionFinder;
import com.favn.ambulance.services.direction.DirectionFinderListener;
import com.favn.ambulance.services.direction.Leg;
import com.favn.ambulance.services.location.LocationChangeListener;
import com.favn.ambulance.services.location.LocationFinder;
import com.favn.ambulance.services.location.LocationStatus;
import com.favn.ambulance.utils.Constants;
import com.favn.ambulance.utils.NetworkStatus;
import com.favn.ambulance.utils.SharedPreferencesData;
import com.favn.ambulance.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class TaskActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationChangeListener, DirectionFinderListener, View.OnClickListener, CallerInformationGetterListener {
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

    private CardView cvDirection;
    private LinearLayout llLoadingStatus;
    private LinearLayout llDirectionResult;
    private Button btnShowCallerDirection;
    private Button btnShowDirection;
    private Button btnPickedUpVictim;
    private Button btnClearDirection;
    private TextView tvDistance;
    private TextView tvDuration;
    private boolean isReady = true;
    private Ambulance ambulance;
    private FirebaseHandle firebaseHandle;
    private CallerInformationGetter callerInformationGetter;
    private AddressResultReceiver mResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (googleServiceAvailable()) {
            initMap();
        }

        createLocationFinder();
        createUI();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);

        // Get ambulance info from SharedPreferences
        ambulance = SharedPreferencesData.getAmbulanceData(Constants.SPREFS_AMBULANCE_INFO_KEY);

        // Request Caller info
        try {
            callerInformationGetter = new CallerInformationGetter(this, ambulance.getId());
            callerInformationGetter.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mResultReceiver = new AddressResultReceiver(new Handler());

    }

    private void createUI() {
        cvDirection = (CardView) findViewById(R.id.cardview_direction);
        llLoadingStatus = (LinearLayout) findViewById(R.id.layout_loading_status);
        llDirectionResult = (LinearLayout) findViewById(R.id.layout_direction_result);
        btnClearDirection = (Button) findViewById(R.id.button_clear_direction);
        btnClearDirection.setOnClickListener(this);
        tvDistance = (TextView) findViewById(R.id.textview_distance);
        tvDuration = (TextView) findViewById(R.id.textview_duration);

        btnShowCallerDirection = (Button) findViewById(R.id.button_caller_location);
        btnShowCallerDirection.setOnClickListener(this);
        btnShowDirection = (Button) findViewById(R.id.button_show_direction);
        btnShowDirection.setOnClickListener(this);
        btnPickedUpVictim = (Button) findViewById(R.id.button_picked_up);
        btnPickedUpVictim.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_screen_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.my_location:
                zoomCurrentLocation();
                break;
            case R.id.report_problem:
                reportProblem();
                break;
            case R.id.finish_task:
                createFinishTaskDialog();
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


    }

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
                    if (directionFinder != null) {
                        directionFinder.stop();
                    }

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

    // Override LocationChangeListener interface
    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(TaskActivity.this, Constants.REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    //TODO send ambulance info
    @Override
    public void locationChangeSuccess(Location location) {
        mCurrentLocation = location;

        if(isNetworkEnable && mCurrentLocation != null) {
            TaskReporter taskReporter = new TaskReporter();
            taskReporter.sendLocation(ambulance.getId(), mCurrentLocation);
        }
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
                .setPositiveButton("CÀI ĐẶT", new DialogInterface.OnClickListener() {
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
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_caller));

        destinationMarkers = mGoogleMap.addMarker(markerOptions);
        destinationMarkers.showInfoWindow();
    }

    private void goToLocationZoom(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void zoomCurrentLocation() {
        if (mCurrentLocation != null) {
            goToLocationZoom(getLatLng(mCurrentLocation), Constants.ZOOM_LEVEL_15);
        }
    }

    private void zoomDestinationLocation() {
        if (destinationLanLng != null) {
            goToLocationZoom(destinationLanLng, Constants.ZOOM_LEVEL_15);
        }
    }

    private void showRouteOverview() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(getLatLng(mCurrentLocation));
        builder.include(destinationLanLng);
        LatLngBounds bounds = builder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, Constants
                .PADDING_420));
    }

    private LatLng getLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
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
        if (isNetworkEnable) {
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
        removePolylinePath();
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true)
                .color(getResources().getColor(R.color.colorBlue))
                .width(10);

        for (int i = 0; i < leg.getListLatLng().size(); i++)
            polylineOptions.add(leg.getListLatLng().get(i));

        polylinePaths = mGoogleMap.addPolyline(polylineOptions);
        updateDirectionUI(leg.getDistance().getText(), leg.getDuration().getText());
        showRouteOverview();
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
            case R.id.button_caller_location:
                zoomDestinationLocation();
                break;
            case R.id.button_show_direction:
                sendDirectionRequest();
                break;
            case R.id.button_picked_up:
                createPickedUpConfirmDialog();
                break;
            case R.id.button_clear_direction:
                clearDirection();
                break;
        }
    }

    private void createPickedUpConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Đã đón nạn nhân")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        TaskReporter taskReporter = new TaskReporter();
                        taskReporter.pickedCaller(ambulance.getId());
                    }
                })
                .create()
                .show();
    }

    private void clearDirection() {
        removePolylinePath();
        directionFinder.stop();
        manageDirectionUI(false);

    }

    private void removePolylinePath() {
        if (polylinePaths != null) {
            polylinePaths.remove();
        }
    }

    // create this method 10/12

    private void createFinishTaskDialog() {
        View checkBoxView = View.inflate(this, R.layout.checkbox, null);
        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isReady = isChecked;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.checkbox_title);
        builder.setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String status = null;
                        if (isReady) {
                            status = Constants.AMBULANCE_STATUS_READY;
                        } else {
                            status = Constants.AMBULANCE_STATUS_BUZY;
                        }
                        SharedPreferencesData.saveData(getBaseContext(), Constants.SPREFS_NAME, Constants
                                .SPREFS_AMBULANCE_STATUS_KEY, status);
                        goBackToWaitingActivity(isReady);

                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void goBackToWaitingActivity(boolean status) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra("isReady", status);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    //TODO : Report picked up caller
    public void reportPickupCaller(String ambulanceID) {
        removeItemsOnMap();
    }

    // Remove marker and polyline on Map
    private void removeItemsOnMap() {
        if (destinationMarkers != null) {
            destinationMarkers.remove();
        }
        if (polylinePaths != null) {
            polylinePaths.remove();
        }
    }

    @Override
    public void getCallerInformationSuccess(Caller caller) {
        destinationLanLng = new LatLng(caller.getLatitude(), caller.getLongitude());
        createMarker(destinationLanLng, "this is address");
        zoomDestinationLocation();
        Location callerLocation = new Location("caller_location");
        callerLocation.setLatitude(caller.getLatitude());
        callerLocation.setLongitude(caller.getLongitude());

        if (isNetworkEnable) {
            startAddressIntentService(callerLocation);
        }
        updateEmergencyInfo(caller);
    }

    private void startAddressIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private void updateEmergencyInfo(Caller caller) {
        TextView tvPhoneNumber = (TextView) findViewById(R.id.textview_phone_number);
        tvPhoneNumber.setText(caller.getPhone());



        TextView tvDescription = (TextView) findViewById(R.id.textview_description);
        tvDescription.setText(caller.getSymptom());
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Set address if was found.
            TextView tvLocation = (TextView) findViewById(R.id.textview_location);
            String mAddress = resultData.getString(Constants.RESULT_DATA_KEY);
            Toast.makeText(getBaseContext(), mAddress + "", Toast.LENGTH_SHORT).show();

            if (resultCode == Constants.SUCCESS_RESULT) {
                 mAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                tvLocation.setText(mAddress);
            } else {
                tvLocation.setText("Không xác định được địa chỉ.");
            }
        }
    }
}
