package com.favn.firstaid.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class Constants {

    /**
     * Location, Map, Direction
     */

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

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static final int UPDATE_SMALLEST_DISPLACEMENT = 20;

    // Numeric in m unit
    public static final double EARTH_RADIUS = 6371000;

    public static final double SEARCH_RADIUS = 20000; // radius ro health facilities

    /**
     * End Location, Map, Direction
     */

    // Add Constants for detect what control(listview, spinner,..) - added by Kienmt : 11/04/2016
    public static final int LISTVIEW_EMERGENCY = 1;

    public static final int SPINNER_INJURY = 2;
    // End add constant

    // Intent filter
    public static final String INTENT_FILTER_PROVIDERS_CHANGED = "android.location" +
            ".PROVIDERS_CHANGED";
    public static final String INTENT_FILTER_CONNECTIVITY_CHANGE = "android.net.conn" +
            ".CONNECTIVITY_CHANGE";
    public static final String INTENT_FILTER_WIFI_STATE_CHANGED = "android.net.wifi" +
            ".WIFI_STATE_CHANGED";


    // Location text
    public static final String LOCATION_VI_TRI = "Vị trí";

    public static final String LOCATION_NO_RESULT_KHONG_RO_VI_TRI = "Không rõ vị trí";

    // Warning
    public static final String WARNING_NO_GPS = "Không xác định được vị trí";

    public static final String WARNING_NO_NETWORK_RESULT = "Bệnh viện trong bán kính 20 km";

    public static final String WARNING_NO_RESULT = "Không tìm thấy bệnh viện trong bán kính 20 km";

    // Filter Health Facility
    public static final String FILTER_HOSPITAL = "1";

    public static final String FILTER_MEDICINE_CENTER = "2";

    // Number 115
    public static final String CALL_115 = "tel:900"; // value is set to 900 for beta version

    // Text for notify, warning for sendind information functionality
    public static final String INFO_WARNING_CONNECTIVITY = "Không có kết nối";

    public static final String INFO_ENABLE_CONNECTIVITY = "Có thể gửi thông tin";

    public static final String INFO_SENDING_INFORMATION = "Đang gửi thông tin";

    public static final String INFO_ERROR_SENDING_INFORMATION = "Không gửi được thông tin";

    public static final String INFO_SUCCESS_SENDING_INFORMATION = "Đã gửi thành công";

    // type of Action when jump activity
    public static final int FROM_EMERGENCY = 1;
    public static final int FROM_LEARNING = 2;
}
