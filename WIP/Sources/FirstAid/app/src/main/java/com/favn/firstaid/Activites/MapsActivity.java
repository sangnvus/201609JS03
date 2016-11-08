package com.favn.firstaid.Activites;

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
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.firstaid.Adapter.HealthFacilityAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Common.Constant;
import com.favn.firstaid.Models.Common.NetworkStatus;
import com.favn.firstaid.Models.Direction.DirectionFinder;
import com.favn.firstaid.Models.Direction.DirectionFinderListener;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinder;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinderListener;
import com.favn.firstaid.Models.FetchAddressIntentService;
import com.favn.firstaid.Models.HealthFacility;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener, DistanceMatrixFinderListener, ResultCallback<LocationSettingsResult> {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    protected Location mLastLocation;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private LinearLayout llCurrentLocation;
    private LinearLayout llHospitalDestination;
    private ImageView imgGpsStatus;
    private TextView tvCurrentLocation;
    private TextView tvLatLng;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ProgressBar mProgressBarLocation;
    private CheckBox chkHospital;
    private TextView tvNotify;


    protected LocationSettingsRequest mLocationSettingsRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int UPDATE_SMALLEST_DISPLACEMENT = 10;
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
    private boolean isFilterHospital;

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


    }

    private void createUIWidget() {
        llCurrentLocation = (LinearLayout) findViewById(R.id.layout_current_location);
        tvCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        imgGpsStatus = (ImageView) findViewById(R.id.image_gps_status);
        tvLatLng = (TextView) findViewById(R.id.text_current_latlng);
        tvNotify = (TextView) findViewById(R.id.textview_notify);

        isFilterHospital = false;
        // Action zoom to current location
        llCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationSettings();
                if (mCurrentLocation != null) {
                    goToCurrentLocationZoom();
                }
            }
        });
        LinearLayout BottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(BottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    getHealthFacilityData();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        chkHospital = (CheckBox) findViewById(R.id.checkbox_filter_hospital);

        chkHospital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFilterHospital = isChecked;
                displayHealthFacility(healthFacilityList);
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
                    startLocationUpdates();
                    updateGpsIcon(GPS_STATUS_NOT_FIXED);
                }

                Toast.makeText(context, "GPS Enabled: " + isGpsProviderEnabled + " Network Location Enabled: " + isNetworkProviderEnabled, Toast.LENGTH_LONG).show();
            } else {
                checkNetworkAvailable(context);
            }
        }
    };

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

        LatLngBounds VIETNAM = new LatLngBounds(Constant.SOUTHWEST, Constant.NORTHEAST);
        mGoogleMap.setLatLngBoundsForCameraTarget(VIETNAM);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VIETNAM.getCenter(), Constant.ZOOM_LEVEL_5));
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
            mLastLocation = mCurrentLocation;
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
            mCurrentLocation = location;
            mLastLocation = mCurrentLocation;
            updateLocationUI();
            updateGpsIcon(GPS_STATUS_FIXED);

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
        intent.putExtra(Constant.RECEIVER, mResultReceiver);
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, mCurrentLocation);
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
                .getLongitude()), Constant.ZOOM_LEVEL_15);
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
            tvCurrentLocation.setText("Không rõ vị trí");
            tvLatLng.setVisibility(View.GONE);
        }

        if (mCurrentLocation != null) {
            tvLatLng.setText("(" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation
                    .getLongitude() + ")");
            tvLatLng.setVisibility(View.VISIBLE);

            if (isAddressFound) {
                tvCurrentLocation.setText(mAddress);
            } else {
                tvCurrentLocation.setText("Vị trí");
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
        if (isLocationEnable && (mCurrentLocation != null) && !isNetworkEnable) {
            healthFacilityList = dbHelper.getListHealthFacility(getPoints());
            displayHealthFacility(healthFacilityList);
            String strNotify = (isNetworkEnable) ? Constant.NOTIFY_ENABLE_NETWORK_RESULT :
                    Constant.NOTIFY_NOT_ENABLE_NETWORK_RESULT;
            tvNotify.setText(strNotify);
        } else if (isLocationEnable && isNetworkEnable) {
            sendDistanceRequest(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude
                    ());
        } else {


        }

    }

    private PointF[] getPoints() {
        PointF[] points = new PointF[4];
        PointF center = new PointF((float) mCurrentLocation.getLatitude(), (float) mCurrentLocation
                .getLongitude());
        double radius = 20000;
        final double mult = 1; // mult = 1.1; is more reliable
        PointF p1 = calculateDerivedPosition(center, mult * radius, 0);
        PointF p2 = calculateDerivedPosition(center, mult * radius, 90);
        PointF p3 = calculateDerivedPosition(center, mult * radius, 180);
        PointF p4 = calculateDerivedPosition(center, mult * radius, 270);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        return points;
    }

    public static PointF calculateDerivedPosition(PointF point,
                                                  double range, double bearing) {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / EarthRadius;
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
        new DistanceMatrixFinder(this, origin, getHospitalsDestinationArray()).execute();
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
    }

    private void displayHealthFacility(List<HealthFacility> healthFacilityList) {
        final ListView lv = (ListView) findViewById(R.id.listview_hospital);

        HealthFacilityAdapter adapter = new HealthFacilityAdapter(this, healthFacilityList);
        if (isFilterHospital) {
            adapter.getFilter().filter(Constant.FILTER_HOSPITAL);
        } else {
            adapter.getFilter().filter(null);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HealthFacility healthFacility = (HealthFacility) lv.getItemAtPosition(position);
                sendDirectionRequest(mCurrentLocation.getLatitude() + "," +
                                mCurrentLocation.getLongitude(),
                        healthFacility.getLatLngText());
            }
        });
    }

    @Override
    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "Finding direction..!", true);
//
//        if (originMarkers != null) {
//            for (Marker marker : originMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (destinationMarkers != null) {
//            for (Marker marker : destinationMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (polylinePaths != null) {
//            for (Polyline polyline:polylinePaths ) {
//                polyline.remove();
//            }
//        }
    }


    @Override
    public void onDirectionFinderSuccess(List<LatLng> latLngs) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

//        for (Route route : routes) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
        mGoogleMap.clear();

        originMarkers.add(mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngs.get(0))));
//            destinationMarkers.add(mGoogleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(route.getLegs()[0].getEnd_location().getLat(),
//                            route.getLegs()[0].getStartLocation().getLng()))));
//
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.BLUE).
                width(10);

        for (int i = 0; i < latLngs.size(); i++)
            polylineOptions.add(latLngs.get(i));

        polylinePaths.add(mGoogleMap.addPolyline(polylineOptions));
//        }
    }

    public HealthFacility[] getHospitalsDestinationArray() {
        healthFacilityList = dbHelper.getListHealthFacility(getPoints());
        HealthFacility hospitalsDestination[] = new HealthFacility[healthFacilityList.size()];
        for (int i = 0; i < healthFacilityList.size(); i++) {
            hospitalsDestination[i] = healthFacilityList.get(i);
        }
        return hospitalsDestination;
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Set address if was found.
            if (resultCode == Constant.SUCCESS_RESULT) {
                mAddress = resultData.getString(Constant.RESULT_DATA_KEY);
                isAddressFound = true;
            } else {
                isAddressFound = false;
            }
        }
    }

}
