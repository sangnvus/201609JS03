package com.favn.firstaid.models.Direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderSuccess(List<LatLng> latLngs);
}
