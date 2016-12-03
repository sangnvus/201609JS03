package com.favn.firstaid.models.DistanceMatrix;

import com.favn.firstaid.models.Commons.Distance;
import com.favn.firstaid.models.Direction.Duration;

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
