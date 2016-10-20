package com.favn.firstaid.Models;

/**
 * Created by Hung Gia on 10/18/2016.
 */

public class Hospital {
    private String name;
    private double latitude;
    private double longitude;

    public Hospital(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLatLngText() {
        return latitude + "," + longitude;
    }
}
