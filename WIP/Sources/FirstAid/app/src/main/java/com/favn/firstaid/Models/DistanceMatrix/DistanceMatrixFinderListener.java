package com.favn.firstaid.Models.DistanceMatrix;

import com.favn.firstaid.Models.Hospital;

import java.util.List;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public interface DistanceMatrixFinderListener {
    void onDistanceMatrixFinderSuccess(List<Hospital> hospitalList);
}
