package com.favn.firstaid.Models.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hung Gia on 11/5/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private boolean isNetworkEnable;
    @Override
    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//        if (activeNetwork == null || activeNetwork.getType() == 0) {
//            isNetworkEnable = false;
//        } else {
//            isNetworkEnable = true;
//        }
    }
}
