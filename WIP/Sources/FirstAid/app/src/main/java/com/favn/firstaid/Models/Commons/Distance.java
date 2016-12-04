package com.favn.firstaid.models.Commons;

/**
 * Created by Hung Gia on 10/19/2016.
 */

public class Distance {
    private String text;
    private int value;

    public Distance(String text) {
        this.text = text;
    }

    public Distance(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
