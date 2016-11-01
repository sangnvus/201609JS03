package com.favn.firstaid.Models.Direction;

import com.favn.firstaid.Models.Direction.Direction;
import com.favn.firstaid.Models.Direction.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<LatLng> latLngs);
}