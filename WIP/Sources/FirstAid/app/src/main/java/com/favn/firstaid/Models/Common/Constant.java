package com.favn.firstaid.Models.Common;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class Constant {
    //API string
    public static String DIRECTION_URL_API = "https://maps.googleapis" +
            ".com/maps/api/directions/json?";

    public static String DISTANCE_URL_API = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    //API key
    public static String API_KEY = "AIzaSyDT6ArSqhIvCaLHl95uQkTe6hTpgQEhB_k";

    // Norttheast and southwest for LatLngBounds
    public static LatLng NORTHEAST = new LatLng(23.393395, 109.4689482);

    public static LatLng SOUTHWEST = new LatLng(8.412729499999999, 102.1444099);

    // Zoom level for map camera
    public static int ZOOM_LEVEL_5 = 5;
    public static int ZOOM_LEVEL_15 = 15;

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = "com.favn.firstaid";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    // Intent filter
    public static final String INTENT_FILTER_PROVIDERS_CHANGED = "android.location" +
            ".PROVIDERS_CHANGED";

    public static final String INTENT_FILTER_CONNECTIVITY_CHANGED = "android.net.conn" +
            ".CONNECTIVITY_CHANGE";
}
