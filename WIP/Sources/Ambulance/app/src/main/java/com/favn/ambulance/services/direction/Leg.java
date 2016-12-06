package com.favn.ambulance.services.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Leg {
    private Distance distance;
    private Duration duration;
    private String end_address;
    private End_location end_location;
    private Start_location start_location;
    List<LatLng> listLatLng;

    public Leg(Distance distance, Duration duration, String end_address, End_location
            end_location, Start_location start_location, List<LatLng> listLatLng) {
        this.distance = distance;
        this.duration = duration;
        this.end_address = end_address;
        this.end_location = end_location;
        this.start_location = start_location;
        this.listLatLng = listLatLng;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getEnd_address() {
        return end_address;
    }

    public End_location getEnd_location() {
        return end_location;
    }

    public Start_location getStart_location() {
        return start_location;
    }

    public List<LatLng> getListLatLng() {
        return listLatLng;
    }
}
