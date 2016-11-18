package com.favn.firstaid.activites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.favn.firstaid.adapter.HealthFacilityAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;
import com.favn.firstaid.models.Commons.Constants;
import com.favn.firstaid.models.Commons.NetworkStatus;
import com.favn.firstaid.models.Direction.DirectionFinder;
import com.favn.firstaid.models.Direction.DirectionFinderListener;
import com.favn.firstaid.models.DistanceMatrix.DistanceMatrixFinder;
import com.favn.firstaid.models.DistanceMatrix.DistanceMatrixFinderListener;
import com.favn.firstaid.models.FetchAddressIntentService;
import com.favn.firstaid.models.HealthFacility;
import com.favn.firstaid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import java.util.ArrayList;
import java.util.List;

import static com.favn.firstaid.R.id.textview_hospital_destination;
import static com.favn.firstaid.R.id.textview_hospital_distance;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener, DistanceMatrixFinderListener, ResultCallback<LocationSettingsResult>, View.OnClickListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    protected LatLng mHealthFacilityLatLng;
    private HealthFacility healthFacilityDestination;

    private Marker destinationMarkers;
    private Polyline polylinePaths;

    private LinearLayout llCurrentLocation;
    private RelativeLayout rlHealthFacilityDestination;
    private LinearLayout llHealthFacilityNavigate;
    private LinearLayout llLoadingStatus;
    private ImageView imgGpsStatus;
    private ImageView imgArrow;
    private TextView tvCurrentLocation;
    private TextView tvLatLng;
    private TextView tvHealthFacilityDestination;
    private TextView tvHealthFacilityDistance;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ProgressBar pbLoadingDirection;
    private TextView tvWarning;
    private TextView tvLoadingStatus;
    private Button btnNavigate;
    private Button btnClearDirection;
    private RadioGroup rgFilter;
    private ToggleButton tbAllHealthFacility;
    private ToggleButton tbHospital;
    private ToggleButton tbMedicineCenter;


    protected LocationSettingsRequest mLocationSettingsRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int UPDATE_SMALLEST_DISPLACEMENT = 1000;
    protected final static String LOCATION_KEY = "location-key";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final String INTENT_FILTER_PROVIDERS_CHANGED = "android.location" +
            ".PROVIDERS_CHANGED";
    public static final String INTENT_FILTER_WIFI_STATE_CHANGED = "android.net.wifi" +
            ".WIFI_STATE_CHANGED";
    public static final String INTENT_FILTER_CONNECTIVITY_CHANGE = "android.net.conn" +
            ".CONNECTIVITY_CHANGE";

    private static final int GPS_STATUS_NOT_FIXED = 1;
    private static final int GPS_STATUS_FIXED = 2;
    private static final int GPS_STATUS_OFF = 0;


    protected String mAddress;
    private AddressResultReceiver mResultReceiver;
    // Intent filter
    IntentFilter intentFilter;

    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    private boolean isAddressFound;
    private String filterHealthFacility;

    // Get database
    private HealthFacilityAdapter healthFacilityAdapterAdapter;
    private DatabaseOpenHelper dbHelper;
    List<HealthFacility> healthFacilityList = new ArrayList<HealthFacility>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createUIWidget();

        mResultReceiver = new AddressResultReceiver(new Handler());
        isLocationEnable = false;

        if (googleServiceAvailable()) {
            initMap();
        }

        buildGoogleApiClient();
        buildLocationSettingsRequest();

        intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(INTENT_FILTER_WIFI_STATE_CHANGED);
        intentFilter.addAction(INTENT_FILTER_CONNECTIVITY_CHANGE);

        updateLocationUI();
        dbHelper = new DatabaseOpenHelper(this);

        rgFilter = (RadioGroup) findViewById(R.id.radio_group_filter);
        rgFilter.setOnCheckedChangeListener(ToggleListener);
        tbAllHealthFacility = (ToggleButton) findViewById(R.id.toggle_all_health_facility);
        tbAllHealthFacility.setOnClickListener(ToggleAction);
        tbHospital = (ToggleButton) findViewById(R.id.toggle_hospital);
        tbHospital.setOnClickListener(ToggleAction);
        tbMedicineCenter = (ToggleButton) findViewById(R.id.toggle_medicine_center);
        tbMedicineCenter.setOnClickListener(ToggleAction);

    }


    private void createUIWidget() {
        llCurrentLocation = (LinearLayout) findViewById(R.id.layout_current_location);
        rlHealthFacilityDestination = (RelativeLayout) findViewById(R.id
                .layout_health_facility_destination);
        llHealthFacilityNavigate = (LinearLayout) findViewById(R.id
                .layout_health_facility_navigate);
        llLoadingStatus = (LinearLayout) findViewById(R.id.layout_loading_status);
        tvCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        tvLatLng = (TextView) findViewById(R.id.text_current_latlng);
        tvWarning = (TextView) findViewById(R.id.textview_notify);
        tvHealthFacilityDestination = (TextView) findViewById(textview_hospital_destination);
        tvHealthFacilityDistance = (TextView) findViewById(textview_hospital_distance);
        tvLoadingStatus = (TextView) findViewById(R.id.textview_loading_status);

        imgGpsStatus = (ImageView) findViewById(R.id.image_gps_status);
        imgArrow = (ImageView) findViewById(R.id.image_arrow);

        btnNavigate = (Button) findViewById(R.id.button_navigate);
        btnClearDirection = (Button) findViewById(R.id.button_clear_direction);
        pbLoadingDirection = (ProgressBar) findViewById(R.id.progress_loading_direction);
        filterHealthFacility = "";

        // Action zoom to current location
        llCurrentLocation.setOnClickListener(this);

        LinearLayout BottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(BottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    getHealthFacilityData();
                    imgArrow.setImageResource(R.drawable.ic_arrow_down);
                    warnHealthFacilityResult();
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    imgArrow.setImageResource(R.drawable.ic_arrow_up);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

    }

    // Broadcast for Connectivity status
    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(INTENT_FILTER_PROVIDERS_CHANGED)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager
                        .GPS_PROVIDER);
                boolean isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager
                        .NETWORK_PROVIDER);

                if (isGpsProviderEnabled == false && isNetworkProviderEnabled == false) {
                    // set gps icon to off
                    updateGpsIcon(GPS_STATUS_OFF);
                } else if (isGpsProviderEnabled == true || isNetworkProviderEnabled == true) {
                    isLocationEnable = true;
                    startLocationUpdates();
                    updateGpsIcon(GPS_STATUS_NOT_FIXED);
                }

                Toast.makeText(context, "GPS Enabled: " + isGpsProviderEnabled + " Network Location Enabled: " + isNetworkProviderEnabled, Toast.LENGTH_LONG).show();
            } else {
                checkNetworkAvailable(context);
            }
        }
    };


    //TODO consider to check CONNECTIVITY_CHANGE

    // Set isNetworkEnable value when Network status change
    private void checkNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null || activeNetwork.getType() == 0) {
            isNetworkEnable = false;
        } else {
            isNetworkEnable = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityBroadcastReceiver);
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        LatLngBounds VIETNAM = new LatLngBounds(Constants.SOUTHWEST, Constants.NORTHEAST);
        mGoogleMap.setLatLngBoundsForCameraTarget(VIETNAM);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VIETNAM.getCenter(), Constants.ZOOM_LEVEL_5));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationSettings();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (!Geocoder.isPresent()) {
                return;
            }
            updateLocationUI();
        }
        if (mCurrentLocation != null) {
            goToCurrentLocationZoom();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("connection location", "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
        } else {
            //set isLocationEnable true for showing data
            isLocationEnable = true;
            mCurrentLocation = location;
            updateLocationUI();
            updateGpsIcon(GPS_STATUS_FIXED);
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                getHealthFacilityData();
            }

            if (isNetworkEnable) {
                startAddressIntentService();
            }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(UPDATE_SMALLEST_DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    // Call address intent service
    protected void startAddressIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
        startService(intent);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied.
                isLocationEnable = true;
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. Show the user a dialog to
                // upgrade location settings
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //PendingIntent unable to execute request.
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // User agreed to make required location settings changes.
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    private void buildNetworkSetting() {
        isNetworkEnable = NetworkStatus.checkNetworkEnable(this);
        if (!isNetworkEnable) {
            createNetworkSetting();
        }
    }


    private void goToLocationZoom(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void goToCurrentLocationZoom() {
        goToLocationZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
                .getLongitude()), Constants.ZOOM_LEVEL_15);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
        mAddress = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
    }


    // Update UI
    private void updateGpsIcon(int gpsStatus) {
        switch (gpsStatus) {
            case GPS_STATUS_OFF:
                imgGpsStatus.setImageResource(R.drawable.ic_gps_off);
                break;
            case GPS_STATUS_NOT_FIXED:
                imgGpsStatus.setImageResource(R.drawable.ic_gps_not_fixed);
                break;
            case GPS_STATUS_FIXED:
                imgGpsStatus.setImageResource(R.drawable.ic_gps_fixed);
                break;
        }
    }

    private void updateLocationUI() {
        if (!isLocationEnable && !isNetworkEnable && mCurrentLocation == null) {
            tvCurrentLocation.setText(Constants.LOCATION_NO_RESULT_KHONG_RO_VI_TRI);
            tvLatLng.setVisibility(View.GONE);
        }

        if (mCurrentLocation != null) {
            tvLatLng.setText("(" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation
                    .getLongitude() + ")");
            tvLatLng.setVisibility(View.VISIBLE);

            if (isAddressFound) {
                tvCurrentLocation.setText(mAddress);
            } else {
                tvCurrentLocation.setText(Constants.LOCATION_VI_TRI);
            }
        }
    }


    // Map, Distance, Direction implements

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

    private void getHealthFacilityData() {
        if (isLocationEnable && (mCurrentLocation != null)) {
            updateLoadingUI(true);
            healthFacilityList = dbHelper.getListHealthFacility(getPoints());
            if (healthFacilityList.size() != 0) {

                if (isNetworkEnable) {
                    sendDistanceRequest(mCurrentLocation.getLatitude() + "," + mCurrentLocation
                            .getLongitude());
                } else {
                    displayHealthFacility(healthFacilityList);
                }
            } else {
                updateLoadingUI(false);
                tvWarning.setText("Không tìm thấy bệnh viện trong bán kính 20 km");
                tvWarning.setVisibility(View.VISIBLE);
            }

        }
    }

    private void warnHealthFacilityResult() {
        if (!isLocationEnable) {
            tvWarning.setText(Constants.WARNING_NO_GPS);
            tvWarning.setVisibility(View.VISIBLE);
        } else if (!isNetworkEnable) {
            tvWarning.setText(Constants.WARN_NO_NETWORK_RESULT);
            tvWarning.setVisibility(View.VISIBLE);
        } else if (healthFacilityList.size() == 0) {
            tvWarning.setText("Không có bệnh viện trong bán kính 20 km");
            tvWarning.setVisibility(View.VISIBLE);
        } else {
            tvWarning.setVisibility(View.GONE);
        }
    }

    private PointF[] getPoints() {
        PointF[] points = new PointF[4];
        PointF center = new PointF((float)mCurrentLocation.getLatitude(), (float)mCurrentLocation
                .getLongitude());

        final double mult = 1.1; // mult = 1.1; is more reliable
        PointF p1 = calculateDerivedPosition(center, mult * Constants.SEARCH_RADIUS, 0);
        PointF p2 = calculateDerivedPosition(center, mult * Constants.SEARCH_RADIUS, 90);
        PointF p3 = calculateDerivedPosition(center, mult * Constants.SEARCH_RADIUS, 180);
        PointF p4 = calculateDerivedPosition(center, mult * Constants.SEARCH_RADIUS, 270);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        return points;
    }

    public static PointF calculateDerivedPosition(PointF point, double range, double bearing) {
        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / Constants.EARTH_RADIUS;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        PointF newPoint = new PointF((float) lat, (float) lon);

        return newPoint;

    }

    private void sendDistanceRequest(String origin) {
        new DistanceMatrixFinder(this, origin, getHealthFacilityDestinationArray()).execute();
        updateLoadingUI(true);
    }

    private void sendDirectionRequest(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDistanceMatrixFinderSuccess(List<HealthFacility> healthFacilityListResult) {
        healthFacilityList = healthFacilityListResult;
        displayHealthFacility(healthFacilityList);
        updateLoadingUI(false);
    }

    private void displayHealthFacility(List<HealthFacility> healthFacilityList) {
        final ListView lv = (ListView) findViewById(R.id.listview_hospital);

        HealthFacilityAdapter adapter = new HealthFacilityAdapter(this, healthFacilityList);
        if (filterHealthFacility.equals(Constants.FILTER_HOSPITAL)) {
            adapter.getFilter().filter(Constants.FILTER_HOSPITAL);
        } else if (filterHealthFacility.equals(Constants.FILTER_MEDICINE_CENTER)) {
            adapter.getFilter().filter(Constants.FILTER_MEDICINE_CENTER);
        } else {
            adapter.getFilter().filter(null);
        }
        updateLoadingUI(false);
        warnHealthFacilityResult();
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                healthFacilityDestination = (HealthFacility) lv.getItemAtPosition(position);
                LatLng latLng = new LatLng(healthFacilityDestination.getLatitude(), healthFacilityDestination
                        .getLongitude());

                removeItemsOnMap();

                updateDestinationUI(healthFacilityDestination);
                mHealthFacilityLatLng = latLng;
                goToLocationZoom(latLng, Constants.ZOOM_LEVEL_15);

                createMarker(latLng, healthFacilityDestination.getName());
                btnClearDirection.setVisibility(View.GONE);
                btnNavigate.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void updateDestinationUI(HealthFacility healthFacility) {


        btnNavigate.setOnClickListener(this);

        tvHealthFacilityDestination.setText(healthFacility.getName());
        if (healthFacility.getDistance() != null) {
            tvHealthFacilityDistance.setText(healthFacility.getDistance().getText());
            tvHealthFacilityDistance.setVisibility(View.VISIBLE);
            llHealthFacilityNavigate.setVisibility(View.VISIBLE);
        } else {
            tvHealthFacilityDistance.setVisibility(View.GONE);
        }

        rlHealthFacilityDestination.setVisibility(View.VISIBLE);

        rlHealthFacilityDestination.setOnClickListener(this);

    }

    private void createMarker(LatLng latLng, String healthFacilityName) {

        MarkerOptions markerOptions = new MarkerOptions()
                .title(healthFacilityName)
                .position(latLng);

        destinationMarkers = mGoogleMap.addMarker(markerOptions);
        destinationMarkers.showInfoWindow();
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
    public void onDirectionFinderSuccess(List<LatLng> latLngs) {
        pbLoadingDirection.setVisibility(View.GONE);
        btnClearDirection.setVisibility(View.VISIBLE);
        btnClearDirection.setOnClickListener(this);
        goToCurrentLocationZoom();
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true)
                .color(getResources().getColor(R.color.colorGpsFixed))
                .width(10);

        for (int i = 0; i < latLngs.size(); i++)
            polylineOptions.add(latLngs.get(i));

        polylinePaths = mGoogleMap.addPolyline(polylineOptions);
    }

    public HealthFacility[] getHealthFacilityDestinationArray() {
        healthFacilityList = dbHelper.getListHealthFacility(getPoints());
        HealthFacility hospitalsDestination[] = new HealthFacility[healthFacilityList.size()];
        for (int i = 0; i < healthFacilityList.size(); i++) {
            hospitalsDestination[i] = healthFacilityList.get(i);
        }
        return hospitalsDestination;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_current_location:
                zoomCurrentLocation();
                break;
            case R.id.layout_health_facility_destination:
                zoomDestinationLocation();
                break;
            case R.id.button_navigate:
                navigate();
                break;
            case R.id.button_clear_direction:
                clearDirection();
        }
    }

    private void zoomCurrentLocation() {
        checkLocationSettings();
        if (mCurrentLocation != null) {
            goToCurrentLocationZoom();
        }
    }

    private void zoomDestinationLocation() {
        if (mHealthFacilityLatLng != null) {
            goToLocationZoom(mHealthFacilityLatLng, Constants.ZOOM_LEVEL_15);
        }
    }

    private void navigate() {
        buildNetworkSetting();
        if (isNetworkEnable) {
            pbLoadingDirection.setVisibility(View.VISIBLE);
            btnNavigate.setVisibility(View.GONE);
            sendDirectionRequest(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude(),
                    healthFacilityDestination.getLatLngText());
        }
    }

    private void clearDirection() {
        btnClearDirection.setVisibility(View.GONE);
        btnNavigate.setVisibility(View.VISIBLE);
        removeItemsOnMap();
        rlHealthFacilityDestination.setVisibility(View.GONE);
    }

    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        }
    };

    // Filter Health Facility
    private ToggleButton.OnClickListener ToggleAction = new ToggleButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            rgFilter.clearCheck();
            switch (v.getId()) {
                case R.id.toggle_all_health_facility:
                    rgFilter.check(R.id.toggle_all_health_facility);
                    filterHealthFacility = "";
                    break;
                case R.id.toggle_hospital:
                    rgFilter.check(R.id.toggle_hospital);
                    filterHealthFacility = Constants.FILTER_HOSPITAL;
                    break;
                case R.id.toggle_medicine_center:
                    rgFilter.check(R.id.toggle_medicine_center);
                    filterHealthFacility = Constants.FILTER_MEDICINE_CENTER;
                    break;
            }
            displayHealthFacility(healthFacilityList);
        }
    };

    private void updateLoadingUI(boolean isLoading) {
        if (isLoading) {
            llLoadingStatus.setVisibility(View.VISIBLE);
        } else {
            llLoadingStatus.setVisibility(View.GONE);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Set address if was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                mAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                tvCurrentLocation.setText(mAddress);
                isAddressFound = true;
            } else {
                isAddressFound = false;
            }
        }
    }

}
