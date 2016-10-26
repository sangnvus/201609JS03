package com.favn.firstaid.Models;

import com.favn.firstaid.Models.Common.Distance;

import java.util.Comparator;

/**
 * Created by Hung Gia on 10/18/2016.
 */

public class Hospital {
    private String name;
    private double latitude;
    private double longitude;
    private Distance distance;

    public Hospital(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Hospital(String name, double latitude, double longitude, Distance distance) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Distance getDistance() {
        return distance;
    }

    public String getLatLngText() {
        return latitude + "," + longitude;
    }
}
