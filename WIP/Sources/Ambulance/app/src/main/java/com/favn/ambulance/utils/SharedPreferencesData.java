package com.favn.ambulance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.favn.ambulance.commons.Ambulance;

/**
 * Created by Hung Gia on 11/28/2016.
 */

public class SharedPreferencesData {
    static SharedPreferences sharedPreferences;

    public static void saveData(Context context, String SPName, String ambulanceInfoKey, String
            ambulanceInfoData) {
        sharedPreferences = context.getSharedPreferences(SPName, context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ambulanceInfoKey, ambulanceInfoData);
        editor.commit();
    }

    public static Ambulance getAmbulanceData(String ambulanceInfoKey) {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(ambulanceInfoKey, "");
        Ambulance ambulance = null;
//        try {
//            ambulance = gson.fromJson(json, Ambulance.class);
//        } catch (JsonParseException e) {
//        }
        return ambulance;
    }

    // Call this method when logout
    public static void clearData(Context context, String SPName, String ambulanceInfoKey) {
        sharedPreferences = context.getSharedPreferences(SPName, context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ambulanceInfoKey, "");
        editor.commit();
    }

}
