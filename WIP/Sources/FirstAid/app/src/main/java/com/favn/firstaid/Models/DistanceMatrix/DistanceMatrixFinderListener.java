package com.favn.firstaid.models.DistanceMatrix;

import com.favn.firstaid.models.HealthFacility;

import java.util.List;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public interface DistanceMatrixFinderListener {
    void onDistanceMatrixFinderSuccess(List<HealthFacility> healthFacilityList);
}
