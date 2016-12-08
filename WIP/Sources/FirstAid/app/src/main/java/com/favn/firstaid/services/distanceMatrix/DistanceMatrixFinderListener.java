package com.favn.firstaid.services.distanceMatrix;

import com.favn.firstaid.commons.HealthFacility;

import java.util.List;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public interface DistanceMatrixFinderListener {
    void onDistanceMatrixFinderSuccess(List<HealthFacility> healthFacilityList);
}
