package com.favn.ambulance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.favn.ambulance.commons.Ambulance;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by Hung Gia on 11/28/2016.
 */

public class SharedPreferencesData {
    static SharedPreferences sharedPreferences;

    public static void saveData(Context context, String SPName, String key, String value) {
        sharedPreferences = context.getSharedPreferences(SPName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static Ambulance getAmbulanceData(String ambulanceInfoKey) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ambulanceInfoKey, "");
        Ambulance ambulance = null;
        try {
            ambulance = gson.fromJson(json, Ambulance.class);
        } catch (JsonParseException e) {
        }
        return ambulance;
    }

    public static String getAmbulanceStatus() {
        return sharedPreferences.getString(Constants.SPREFS_AMBULANCE_STATUS_KEY, "");
    }

    // Call this method when logout
    public static void clearData(Context context, String SPName, String ambulanceInfoKey) {
        sharedPreferences = context.getSharedPreferences(SPName, context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ambulanceInfoKey, "");
        editor.commit();
    }

}
