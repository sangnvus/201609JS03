package com.favn.firstaid.models.Direction;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public class Direction {

    public Route routes[];
    private String status;
    public Direction(Route[] routes, String status) {
        this.routes = routes;
        this.status = status;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public String getStatus() {
        return status;
    }
}
