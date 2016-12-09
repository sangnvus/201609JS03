package com.favn.firstaid.services.distanceMatrix;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class DistanceMatrix {
    private String destination_addresses[];
    private Rows[] rows;

    public DistanceMatrix(String[] destination_addresses, Rows[] rows) {
        this.destination_addresses = destination_addresses;
        this.rows = rows;
    }

    public String[] getDestination_addresses() {
        return destination_addresses;
    }

    public Rows[] getRows() {
        return rows;
    }
}
