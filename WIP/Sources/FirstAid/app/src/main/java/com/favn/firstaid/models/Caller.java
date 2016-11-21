package com.favn.firstaid.models;

/**
 * Created by KienMT on 11/21/2016.
 */

public class Caller {
    public String phone;
    public int injuryId;
    public double latitude;
    public double longitude;

    // Use to get data from firebase
    public Caller() {

    }

    public Caller(String phone, int injuryId, double latitude, double longitude) {
        this.phone = phone;
        this.injuryId = injuryId;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
