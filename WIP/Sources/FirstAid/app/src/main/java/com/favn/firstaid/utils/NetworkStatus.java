package com.favn.firstaid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hung Gia on 11/5/2016.
 */

public class NetworkStatus {

    public static boolean checkNetworkEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}

