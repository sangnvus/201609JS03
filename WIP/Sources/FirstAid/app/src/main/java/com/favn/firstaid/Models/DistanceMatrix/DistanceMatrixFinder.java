package com.favn.firstaid.models.DistanceMatrix;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.favn.firstaid.models.Commons.Constants;
import com.favn.firstaid.models.Commons.Distance;
import com.favn.firstaid.models.HealthFacility;
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
    private List<HealthFacility> healthFacilityList;

    public DistanceMatrixFinder(DistanceMatrixFinderListener listener, String origin,
                                List<HealthFacility> destinations) {
        this.listener = listener;
        this.origin = origin;
        this.healthFacilityList = destinations;
    }

    public void execute() {
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() {
        String url = "";
        String destinationLocation = "";
        for (int i = 0; i < healthFacilityList.size(); i++) {
            destinationLocation += healthFacilityList.get(i).getLatLngText();
            if (i < healthFacilityList.size() - 1) {
                destinationLocation += "|";
            }
        }
        url = Constants.DISTANCE_URL_API + "origins=" + origin + "&destinations=" +
                destinationLocation + "&language=vi&key=" + Constants.API_KEY;
        return url;
    }

    private class DownloadRawData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String link = params[0];

            try {

                URL url = new URL(link);
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
                DistanceMatrix results = new Gson().fromJson(reader, DistanceMatrix.class);
                if (results.getRows().length > 0) {
                    for (int i = 0; i < results.getRows()[0].getElements().length; i++) {
                        String status = results.getRows()[0].getElements()[0].getStatus();

                        if (status.equals("OK")) {
                            healthFacilityList.get(i).setDistance(results.getRows()[0]
                                    .getElements()[i]
                                    .getDistance());
                            healthFacilityList.get(i).setDuration(results.getRows()[0].getElements()[i].getDuration());
                        }else{
                            healthFacilityList.get(i).setDistance(new Distance("NO_RESULT"));
                        }

                    }
                } else {
                    return null;
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
        protected void onPostExecute(Void avoid) {
            listener.onDistanceMatrixFinderSuccess(healthFacilityList);
        }
    }
}
