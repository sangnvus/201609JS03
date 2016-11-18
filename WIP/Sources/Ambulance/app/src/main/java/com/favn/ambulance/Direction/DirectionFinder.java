package com.favn.ambulance.direction;

import android.os.AsyncTask;
import android.util.Log;

import com.favn.ambulance.models.Commons.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Gia on 10/15/2016.
 */

public class DirectionFinder {
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
    List<LatLng> listLatLng;
    Leg leg;
    DownloadRawData downloadRawData;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public void execute() throws UnsupportedEncodingException {
        downloadRawData = new DownloadRawData();
        downloadRawData.execute(createUrl());
    }

    public void stop() {
        downloadRawData.cancel(true);
    }

    private String createUrl() throws UnsupportedEncodingException {
        Log.d("destination", destination + "");
        String url = "";
        url = Constants.DIRECTION_URL_API + "origin=" + origin + "&destination=" +
                destination + "&language=vi&key=" + Constants.API_KEY;

        Log.d("destination", url);
        return url;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
                Direction results = new Gson().fromJson(reader, Direction.class);

                listLatLng = decodePolyLine(results.getRoutes()[0].getOverviewPolyline().getPoints());
                leg = new Leg(results.getRoutes()[0].getLegs()[0].getDistance(),
                        results.getRoutes()[0].getLegs()[0].getDuration(),
                        results.getRoutes()[0].getLegs()[0].getEnd_address(),
                        results.getRoutes()[0].getLegs()[0].getEnd_location(),
                        results.getRoutes()[0].getLegs()[0].getStart_location(),
                        listLatLng);

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
                listener.onDirectionFinderSuccess(leg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
