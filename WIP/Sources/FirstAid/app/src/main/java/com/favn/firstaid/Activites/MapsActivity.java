package com.favn.firstaid.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.location.Address;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.firstaid.Adapter.HospitalAdapter;
import com.favn.firstaid.Models.Common.Constant;
import com.favn.firstaid.Models.Direction.DirectionFinder;
import com.favn.firstaid.Models.Direction.DirectionFinderListener;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinder;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinderListener;
import com.favn.firstaid.Models.Hospital;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener, DistanceMatrixFinderListener, ResultCallback<LocationSettingsResult> {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LatLng currentLatLng;
    protected Boolean mRequestingLocationUpdates;
    protected Location mLastLocation;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private List<Hospital> hospitalList = new ArrayList<>();

    private LinearLayout llCurrentLocation;
    private LinearLayout llHospitalDestination;
    private TextView tvCurrentLocation;
    private TextView tvLatLng;
    private TextView tvUpdateTime;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ProgressBar mProgressBarLocation;
    private Button btnFindHospital;
    protected LocationSettingsRequest mLocationSettingsRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int UPDATE_SMALLEST_DISPLACEMENT = 10;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (googleServiceAvailable()) {
            setContentView(R.layout.activity_maps);
            initMap();
        }


        llCurrentLocation = (LinearLayout) findViewById(R.id.layout_curent_location);
        llCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocationZoom(currentLatLng, Constant.ZOOM_LEVEL_15);
            }
        });

        // View bottomSheet = findViewById(R.id.bottom_sheet_hospital_list);
        // mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior.setPeekHeight(300);
//        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mockup();
//                    Log.d("test", "test");
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });

        // llHospitalDestination = (LinearLayout) findViewById(R.id.layout_hospital_destination);


        tvCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        tvLatLng = (TextView) findViewById(R.id.text_current_latlng);
        tvUpdateTime = (TextView) findViewById(R.id.text_update_time);

        btnFindHospital = (Button) findViewById(R.id.test);
        btnFindHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    geoLocate();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
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

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.d("location code", "Updating values from bundle");
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);

            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
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
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
//        builder.setAlwaysShow(true);
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(
//                        mGoogleApiClient,
//                        builder.build()
//                );
//
//        result.setResultCallback(this);
//        startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }
        Log.d("connection", "onlocation connect");

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
        Log.d("connection", "onlocation change");
        Toast.makeText(this, "onlocation change", Toast.LENGTH_LONG
        ).show();
        if (location == null) {
            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        } else {
            mCurrentLocation = location;
            mLastLocation = mCurrentLocation;
            updateUI();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
       // mLocationRequest.setSmallestDisplacement(UPDATE_SMALLEST_DISPLACEMENT);
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

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
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

    private void sendDistanceRequest(String origin) {
        new DistanceMatrixFinder(this, origin, getHospitalsDestination()).execute();
    }

    private void sendDirectionRequest(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    public Hospital[] getHospitalsDestination() {
        List<Hospital> hospitalList = new ArrayList<Hospital>();
        hospitalList.add(new Hospital("National Hospital of Obstetrics and Gynecology", 21.0093735, 105.5307141));
        hospitalList.add(new Hospital("National Hospital of Traditional Medicine", 21.009574, 105.5209513));
        hospitalList.add(new Hospital("Hanoi Heart Hospital", 21.017933, 105.5318903));
        hospitalList.add(new Hospital("Bệnh viện Đa Khoa Hà Nội", 20.999166, 105.5285813));
        hospitalList.add(new Hospital("Bệnh Viện Mắt Kỹ Thuật Cao HN", 21.011349, 105.5235913));
        hospitalList.add(new Hospital("Bệnh viện đa khoa huyện Hải Hà", 21.0093735, 105.5307141));
        hospitalList.add(new Hospital("BV def", 21.009574, 105.5209513));
        hospitalList.add(new Hospital("BV ghi", 21.017933, 105.5318903));
        hospitalList.add(new Hospital("BV klm", 20.999166, 105.5285813));
        hospitalList.add(new Hospital("BV klm", 21.011349, 105.5235913));

        Hospital hospitalsDestination[] = new Hospital[hospitalList.size()];
        for (int i = 0; i < hospitalList.size(); i++) {
            hospitalsDestination[i] = hospitalList.get(i);
        }
        return hospitalsDestination;
    }


    @Override
    public void onDistanceMatrixFinderSuccess(List<Hospital> hospitalList) {

        final ListView lv = (ListView) findViewById(R.id.listview_hospital);

        HospitalAdapter adapter = new HospitalAdapter(this, hospitalList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hospital hospital = (Hospital) lv.getItemAtPosition(position);
                sendDirectionRequest(currentLatLng.latitude + "," + currentLatLng.longitude,
                        hospital.getLatLngText());
            }
        });


    }

    private void goToLocationZoom(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.animateCamera(cameraUpdate);
    }


    private void geoLocate() throws IOException {
        TextView txtCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        txtCurrentLocation.setText("test");
        TextView txtLatLng = (TextView) findViewById(R.id.text_current_latlng);
        txtLatLng.setText("(" + currentLatLng.latitude + ", " + currentLatLng.longitude + ")");
        txtLatLng.setVisibility(View.VISIBLE);

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng
                .longitude, 1);
        Address address = addresses.get(0);

        Address returnedAddress;
        StringBuilder strReturnedAddress = new StringBuilder();
        ;
//            try {
//
        if (addresses != null) {
            returnedAddress = addresses.get(0);
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i));
                if (i < returnedAddress.getMaxAddressLineIndex() - 1) {
                    strReturnedAddress.append(", ");
                }
            }
            Log.d("location", strReturnedAddress.toString());
            Log.d("location1", returnedAddress.getAdminArea());
            Log.d("location2", returnedAddress.getCountryName());
            Log.d("location3", returnedAddress.getLocality());
            Log.d("location4", returnedAddress.getPremises());
        } else {
            Log.d("location", "No Address returned!");
        }
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                Log.d("location", "Cannot get Address!");
//            }

    }

    private List<Hospital> getNearbyHospital(LatLng latLng, List<Hospital> hospitalList) {
        double x1 = latLng.latitude;
        double y1 = latLng.longitude;
        return null;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(this, 100);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }

                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            tvCurrentLocation.setText("test");
            tvLatLng.setText("(" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation
                    .getLongitude() + ")");
            tvUpdateTime.setText(mLastUpdateTime);
            tvLatLng.setVisibility(View.VISIBLE);
        } 



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
