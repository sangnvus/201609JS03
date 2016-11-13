package com.example.mikey.ambulance.LocationUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import com.example.mikey.ambulance.Activities.Task;
import com.example.mikey.ambulance.Models.Commons.Constants;

/**
 * Created by Hung Gia on 11/12/2016.
 */

public class LocationStatus {
    public static boolean checkLocationProvider(Context context) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager
                    .GPS_PROVIDER);
            boolean isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager
                    .NETWORK_PROVIDER);
            Toast.makeText(context, "GPS Enabled: " + isGpsProviderEnabled + " Network Location Enabled: " + isNetworkProviderEnabled, Toast.LENGTH_LONG).show();

            if (isGpsProviderEnabled == false && isNetworkProviderEnabled == false) {
                return false;
            } else if (isGpsProviderEnabled == true || isNetworkProviderEnabled == true) {
                return true;
            }

        return false;
    }

}
