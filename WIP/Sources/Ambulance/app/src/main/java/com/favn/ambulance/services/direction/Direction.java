package com.favn.ambulance.services.direction;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public class Direction {

    public Route routes[];

    public Route[] getRoutes() {
        return routes;
    }

    public Direction(Route[] routes) {
        this.routes = routes;
    }

}
