package com.favn.firstaid.Models;

import android.content.Context;
import android.widget.Toast;

import com.favn.firstaid.Models.Common.NetworkStatus;

/**
 * Created by Hung Gia on 11/5/2016.
 */

public class TestNetwork {
    Context context;

    public TestNetwork(Context context) {
        this.context = context;
    }

    public void test() {
        boolean check = NetworkStatus.checkNetworkEnable(context);
        Toast.makeText(context, check + "", Toast.LENGTH_LONG).show();
    }

}
