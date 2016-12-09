package com.favn.firstaid.services.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderSuccess(String status, List<LatLng> latLngs, String distance, String
            duration);
}
