package com.favn.firstaid.services.direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Start_location {
    private double lat;
    private double lng;

    public Start_location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
