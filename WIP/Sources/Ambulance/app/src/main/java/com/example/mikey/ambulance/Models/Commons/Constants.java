package com.example.mikey.ambulance.Models.Commons;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class Constants {
    //API string
    public static String DIRECTION_URL_API = "https://maps.googleapis" +
            ".com/maps/api/directions/json?";

    //API key
    public static String API_KEY = "AIzaSyAcYh4HrrRlHTYxkD0ZGgKEOD3CaK_JERE";

    //Location
    public static final String INTENT_FILTER_PROVIDERS_CHANGED = "android.location" +
            ".PROVIDERS_CHANGED";

    public static final String INTENT_FILTER_CONNECTIVITY_CHANGE = "android.net.conn" +
            ".CONNECTIVITY_CHANGE";

    // Norttheast and southwest for LatLngBounds
    public static LatLng NORTHEAST = new LatLng(23.393395, 109.4689482);

    public static LatLng SOUTHWEST = new LatLng(8.412729499999999, 102.1444099);

    // Zoom level for map camera
    public static int ZOOM_LEVEL_5 = 5;
    public static int ZOOM_LEVEL_15 = 15;
}
