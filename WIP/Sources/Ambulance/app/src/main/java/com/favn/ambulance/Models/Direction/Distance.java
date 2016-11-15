package com.favn.ambulance.Models.Direction;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Distance {
    private String text;
    private int value;

    public Distance(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

}
