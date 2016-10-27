package com.favn.firstaid.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.favn.firstaid.Adapter.HospitalAdapter;
import com.favn.firstaid.Models.Direction.Route;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener, DistanceMatrixFinderListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private List<Hospital> hospitalList = new ArrayList<>();

    Button btNavigate;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServiceAvailable()) {
            Toast.makeText(this, "ready to map", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_maps);
            initMap();
        }

        btNavigate = (Button) findViewById(R.id.button_navigation);
        btNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendRequest();
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.layout_hospital_show);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDistanceRequest();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    public void geoLocate(View view) throws IOException {
//

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(10000);

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
//        if(location == null) {
//            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
//        } else {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//            mGoogleMap.animateCamera(cameraUpdate);
//            sendRequest(latLng);
//        }
    }

    private void sendDistanceRequest() {
        String origin = "21.0120967,105.5248733";
        new DistanceMatrixFinder(this, origin, getHospitalsDestination()).execute();

    }

    private void sendDirectionRequest(String destination) {
        String origin = "21.0120967,105.5248733";

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {

    }


    @Override
    public void onDirectionFinderSuccess(List<LatLng> latLngs) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

//        for (Route route : routes) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
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
        hospitalList.add(new Hospital("BV abc", 21.0093735, 105.5307141));
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

    // Show list of nearest hospital
    private void showDialog(List<Hospital> hospitalList) {
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_hospital_list, null);
        final ListView lv = (ListView) view.findViewById(R.id.listview_hospital);

        HospitalAdapter adapter;
        adapter = new HospitalAdapter(this, hospitalList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hospital hospital = (Hospital) lv.getItemAtPosition(position);
                sendDirectionRequest(hospital.getLatLngText());
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setTitle("Bệnh viện gần đây");
        dialog.show();

    }

    @Override
    public void onDistanceMatrixFinderSuccess(List<Hospital> hospitalList) {
        showDialog(hospitalList);
    }
}
