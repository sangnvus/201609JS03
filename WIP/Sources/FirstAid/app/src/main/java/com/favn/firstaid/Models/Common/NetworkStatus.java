package com.favn.firstaid.Models.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hung Gia on 11/5/2016.
 */

public class NetworkStatus {
    public static int TYPE_NOT_CONNECTED = 0;

    public static boolean checkNetworkEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService
                (Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null || activeNetwork.getType() == TYPE_NOT_CONNECTED) {
            return false;
        } else {
            return true;
        }
    }
}
