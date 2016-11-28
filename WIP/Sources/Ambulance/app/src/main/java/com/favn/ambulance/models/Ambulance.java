package com.favn.ambulance.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by KienMT on 11/28/2016.
 */

public class Ambulance {
    private int user_id;
    private String team;
    private double latitude;
    private double longitude;
    private String status;

    // Use to get data from firebase
    public Ambulance() {
    }

    public Ambulance(int user_id, String team, double latitude, double longitude, String status) {
        this.user_id = user_id;
        this.team = team;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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
            jo.put("user_id", user_id);
            jo.put("team", team);
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
