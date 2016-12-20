package com.favn.ambulance.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.favn.ambulance.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hung Gia on 11/2/2016.
 */

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddress";
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (mReceiver == null) {
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        if (location == null) {
            errorMessage = "Location is null";
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            errorMessage = "No internet";
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid LatLng";
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No address found";
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressList = new ArrayList<String>();

            // Set empty to feature name if it equals Unnamed Road
            String featureName = address.getFeatureName();
            if(address.getAddressLine(0).equals("Unnamed Road")) {
                featureName = "";
            }

            // Run loop from second element
            for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {

                addressList.add(address.getAddressLine(i));
            }
            String addressAfter = TextUtils.join(", ", addressList);
            String addressStringReturn = "";
            if(featureName.length() > 0) {
                addressStringReturn  = featureName + ", "+ addressAfter;
            } else {
                addressStringReturn = addressAfter;
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addressStringReturn);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
