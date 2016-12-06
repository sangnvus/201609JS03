package com.favn.firstaid.models;

import com.favn.firstaid.services.direction.Duration;

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
    private Duration duration;

    public HealthFacility() {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getLatLngText() {
        return latitude + "," + longitude;
    }
}
