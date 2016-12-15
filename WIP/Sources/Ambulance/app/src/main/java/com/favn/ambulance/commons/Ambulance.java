package com.favn.ambulance.commons;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Hung Gia on 11/28/2016.
 */

public class Ambulance {
    private int id;
    private int user_id;
    private String team;
    private double latitude;
    private double longitude;
    private String status;
    private int caller_taking_id;
    private int isDeleted;
    private String updated_at;
    private String created_at;

    public Ambulance() {

    }

    public Ambulance(int id, int user_id, String team) {
        this.id = id;
        this.user_id = user_id;
        this.team = team;
    }

    public Ambulance(int id, String status, double longitude, double latitude, int caller_taking_id) {
        this.id = id;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.caller_taking_id = caller_taking_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCaller_taking_id() {
        return caller_taking_id;
    }

    public void setCaller_taking_id(int caller_taking_id) {
        this.caller_taking_id = caller_taking_id;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // Pack data to send to server
    public String packData() {
        JSONObject jo = new JSONObject();
        StringBuffer sb = new StringBuffer();

        try {
            jo.put("id", id);
            jo.put("status", status);
            jo.put("longitude", longitude);
            jo.put("latitude", latitude);
            jo.put("caller_taking_id", caller_taking_id);


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
