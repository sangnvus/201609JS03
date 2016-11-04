package com.favn.firstaid.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.favn.firstaid.Models.Common.Constant;

/**
 * Created by Hung Gia on 11/3/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(Constant.INTENT_FILTER_CONNECTIVITY_CHANGED));
    }
}
