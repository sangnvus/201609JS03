package com.favn.firstaid.Models.Direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Route {
    private Leg legs[];
    private Overview_polyline overviewPolyline;

    public Route(Leg[] legs, Overview_polyline overviewPolyline) {
        this.legs = legs;
        this.overviewPolyline = overviewPolyline;
    }

    public Leg[] getLegs() {
        return legs;
    }

    public Overview_polyline getOverviewPolyline() {
        return overviewPolyline;
    }
}
