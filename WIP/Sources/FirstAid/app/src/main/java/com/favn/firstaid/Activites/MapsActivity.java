package com.favn.firstaid.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.favn.firstaid.Models.Direction.DirectionFinder;
import com.favn.firstaid.Models.Direction.DirectionFinderListener;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinder;
import com.favn.firstaid.Models.DistanceMatrix.DistanceMatrixFinderListener;
import com.favn.firstaid.Models.Hospital;
import com.favn.firstaid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener, DistanceMatrixFinderListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng currentLatLng;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private List<Hospital> hospitalList = new ArrayList<>();

    LinearLayout layoutCurrentLocation;
    LinearLayout layoutHospitalDestination;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ProgressBar mProgressBarLocation;
    private Button btnFindHospital;
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServiceAvailable()) {
            Toast.makeText(this, "ready to map", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_maps);
            initMap();
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutCurrentLocation = (LinearLayout) findViewById(R.id.layout_curent_location);
        layoutCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocationZoom(currentLatLng, 15);
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

        // layoutHospitalDestination = (LinearLayout) findViewById(R.id.layout_hospital_destination);

        btnFindHospital = (Button) findViewById(R.id.test);
        btnFindHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    isFarfromLastPosition();
                try {
                    geoLocate();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        LocationManager loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        lastLocation = loc.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

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
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(300000);

//
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
        } else {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());


        }
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

    private boolean isFarfromLastPosition() {

        Log.d("distance", lastLocation.distanceTo(lastLocation) +"");
        return false;
    }

    private void geoLocate() throws IOException {
        TextView txtCurrentLocation = (TextView) findViewById(R.id.textview_current_location);
        txtCurrentLocation.setText("test");
        TextView txtLatLng = (TextView) findViewById(R.id.textview_current_latlng);
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
                if(addresses != null) {
                    returnedAddress = addresses.get(0);
                    for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                        if(i < returnedAddress.getMaxAddressLineIndex() - 1) {
                            strReturnedAddress.append(", ");
                        }
                    }
                    Log.d("location", strReturnedAddress.toString());
                    Log.d("location1", returnedAddress.getAdminArea());
                    Log.d("location2", returnedAddress.getCountryName());
                    Log.d("location3", returnedAddress.getLocality());
                    Log.d("location4", returnedAddress.getPremises());
                }
                else{
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
}
