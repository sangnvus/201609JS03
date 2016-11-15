package com.favn.ambulance.Models.Direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class End_location {
    private double lat;
    private double lng;

    public End_location(double lat, double lng) {
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
