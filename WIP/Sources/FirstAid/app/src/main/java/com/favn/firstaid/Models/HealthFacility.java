package com.favn.firstaid.models;

import com.favn.firstaid.models.Commons.Distance;

/**
 * Created by Hung Gia on 10/18/2016.
 */

public class HealthFacility {
    private int id;
    private String name;
    private int type;
    private String address;
    private String vicinity;
    private String phone;
    private double latitude;
    private double longitude;
    private Distance distance;

    public HealthFacility(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HealthFacility(String name, double latitude, double longitude, Distance distance) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public HealthFacility(String name, int type, String address, String vicinity, String phone, double latitude, double longitude, Distance distance) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.vicinity = vicinity;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public HealthFacility(int id, String name, int type, String address, String vicinity, String phone, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.vicinity = vicinity;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getVicinity() {
        return vicinity;
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
