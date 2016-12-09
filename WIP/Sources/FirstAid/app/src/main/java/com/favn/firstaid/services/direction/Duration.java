package com.favn.firstaid.services.direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Duration {
    private String text;
    private double value;

    public Duration(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public double getValue() {
        return value;
    }


}
