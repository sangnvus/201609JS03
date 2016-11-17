package com.favn.ambulance.models.Commons;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MeHaDake on 11/16/2016.
 */

public class Connector {
    public static HttpURLConnection httpUrlPostConnection(String urlAddress){
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set properties
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoInput(true);
            con.setDoOutput(true);

            return con;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
