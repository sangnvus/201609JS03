package com.favn.ambulance.LocationUtil;

import android.location.Location;

import com.google.android.gms.common.api.Status;

/**
 * Created by Hung Gia on 11/12/2016.
 */

public interface LocationChangeListener {
    void createLocationSettingDialog(Status status);
    void locationChangeSuccess(Location location);
}
