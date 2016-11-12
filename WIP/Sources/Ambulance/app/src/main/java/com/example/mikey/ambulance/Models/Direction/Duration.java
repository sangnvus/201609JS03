package com.example.mikey.ambulance.Models.Direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Duration {
    private String text;
    private double value;

    public Duration(String text, double value) {
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
