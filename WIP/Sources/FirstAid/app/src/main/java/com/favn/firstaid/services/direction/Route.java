package com.favn.firstaid.services.direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Route {
    private Leg legs[];
    private Overview_polyline overview_polyline;

    public Route(Leg[] legs, Overview_polyline overviewPolyline) {
        this.legs = legs;
        this.overview_polyline = overviewPolyline;
    }

    public Leg[] getLegs() {
        return legs;
    }

    public Overview_polyline getOverviewPolyline() {
        return overview_polyline;
    }

}
