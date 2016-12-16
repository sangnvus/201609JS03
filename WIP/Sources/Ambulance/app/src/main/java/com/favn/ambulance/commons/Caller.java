package com.favn.ambulance.commons;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by KienMT on 11/21/2016.
 */

public class Caller {
    private String phone;
    private int injuryId;
    private double latitude;
    private double longitude;
    private String status;

    // Use to get data from firebase
    public Caller() {

    }

    public Caller(String phone, int injuryId, double latitude, double longitude, String status) {
        this.phone = phone;
        this.injuryId = injuryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Pack data to send to server
    public String packData() {
        JSONObject jo = new JSONObject();
        StringBuffer sb = new StringBuffer();

        try {
            jo.put("phone", phone);
            jo.put("injuryId", injuryId);
            jo.put("latitude", latitude);
            jo.put("longitude", longitude);
            jo.put("status", status);

            Boolean isFirstValue = true;
            Iterator it = jo.keys();

            do {
                String key = it.next().toString();
                String value = jo.get(key).toString();

                if(isFirstValue) {
                    isFirstValue = false;
                } else {
                    sb.append("&");
                }

                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } while (it.hasNext());

            return sb.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
