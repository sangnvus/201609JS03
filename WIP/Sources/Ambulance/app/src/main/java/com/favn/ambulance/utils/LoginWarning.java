package com.favn.ambulance.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Hung Gia on 12/16/2016.
 */

public class LoginWarning {
    public static void createLoginWarningDialog(Context context, String warningString) {
        new AlertDialog.Builder(context)
                .setTitle("Đăng nhập không thành công")
                .setMessage(warningString)
                .setNegativeButton("OK", null) // dismisses by default
                .create()
                .show();
    }

}
