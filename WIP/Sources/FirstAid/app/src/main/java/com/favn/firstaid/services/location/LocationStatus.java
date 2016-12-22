package com.favn.firstaid.services.location;

import android.content.Context;
import android.location.LocationManager;

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

            if (isGpsProviderEnabled == false && isNetworkProviderEnabled == false) {
                return false;
            } else if (isGpsProviderEnabled == true || isNetworkProviderEnabled == true) {
                return true;
            }

        return false;
    }

}
