package com.favn.firstaid.services.distanceMatrix;

import com.favn.firstaid.commons.Distance;
import com.favn.firstaid.services.direction.Duration;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class Elements {
    private Distance distance;
    private Duration duration;
    private String status;

    public Elements(Distance distance, Duration duration, String status) {
        this.distance = distance;
        this.duration = duration;
        this.status = status;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }
}
