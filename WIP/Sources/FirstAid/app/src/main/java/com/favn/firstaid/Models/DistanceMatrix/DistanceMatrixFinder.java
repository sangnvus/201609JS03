package com.favn.firstaid.Models.DistanceMatrix;

import android.os.AsyncTask;

import com.favn.firstaid.Models.Common.Constant;
import com.favn.firstaid.Models.HealthFacility;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hung Gia on 10/26/2016.
 */

public class DistanceMatrixFinder {
    private DistanceMatrixFinderListener listener;
    private String origin;
    private HealthFacility destinations[];
    private List<HealthFacility> healthFacilityList = new ArrayList<>();

    public DistanceMatrixFinder(DistanceMatrixFinderListener listener, String origin, HealthFacility[]
            destinations) {
        this.listener = listener;
        this.origin = origin;
        this.destinations = destinations;
    }

    public void execute() {
        //listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() {
        String url = "";
        String destinationLocation = "";
        for (int i = 0; i < destinations.length; i++) {
            destinationLocation += destinations[i].getLatLngText();
            if (i < destinations.length - 1) {
                destinationLocation += "|";
            }
        }
        //TODO consider to add mode: driving
        url = Constant.DISTANCE_URL_API + "origins=" + origin + "&destinations=" +
                destinationLocation + "&key=" + Constant.API_KEY;
        return url;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];

            try {

                URL url = new URL(link);
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
                DistanceMatrix results = new Gson().fromJson(reader, DistanceMatrix.class);

                for (int i = 0; i < results.getRows()[0].getElements().length; i++) {
                    HealthFacility healthFacility = new HealthFacility(destinations[i].getName(),
                            destinations[i].getLatitude(),
                            destinations[i].getLongitude(),
                            results.getRows()[0].getElements()[i].getDistance());
                    healthFacilityList.add(healthFacility);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                Collections.sort(healthFacilityList, new Comparator<HealthFacility>() {
                    public int compare(HealthFacility h1, HealthFacility h2) {
                        return h1.getDistance().getValue() - h2.getDistance().getValue();
                    }
                });
                listener.onDistanceMatrixFinderSuccess(healthFacilityList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
