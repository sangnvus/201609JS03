package com.favn.firstaid.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Hung Gia on 11/3/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private boolean isNetworkAvailable;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        isNetworkAvailable = (activeNetwork != null) ?  true : false;

        Log.d("networkchck", isNetworkAvailable + "");
    }
}
