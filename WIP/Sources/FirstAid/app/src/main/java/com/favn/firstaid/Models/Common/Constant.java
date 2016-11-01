package com.favn.firstaid.Models.Common;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class Constant {
    public static String DIRECTION_URL_API = "https://maps.googleapis" +
            ".com/maps/api/directions/json?";
    public static String DISTANCE_URL_API = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static String API_KEY = "AIzaSyDT6ArSqhIvCaLHl95uQkTe6hTpgQEhB_k";

    public static LatLng NORTHEAST = new LatLng(23.393395, 109.4689482);
    public static LatLng SOUTHWEST = new LatLng(8.412729499999999, 102.1444099);

    public static int ZOOM_LEVEL_5 = 5;
    public static int ZOOM_LEVEL_15 = 15;
}
