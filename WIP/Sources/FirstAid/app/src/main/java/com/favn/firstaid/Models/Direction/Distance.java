package com.favn.firstaid.Models.Direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Distance {
    private String text;
    private double value;

    public Distance(String text, double value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public double getValue() {
        return value;
    }

}
