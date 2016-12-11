package com.favn.firstaid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Hung Gia on 12/5/2016.
 */

public class SettingPref {

    public static String loadPhoneNumber(Context context) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isAllowedSendInformation = mySharedPreferences.getBoolean("switch_sending_information", false);
        if (isAllowedSendInformation) {
            return mySharedPreferences.getString("et_phone_number", null);

        }
        return null;
    }
}
