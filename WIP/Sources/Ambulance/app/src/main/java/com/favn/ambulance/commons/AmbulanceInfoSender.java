package com.favn.ambulance.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Struct;

/**
 * Created by KienMT on 11/28/2016.
 */

public class AmbulanceInfoSender extends AsyncTask<Void, Void, String>{
    private Context context;
    private String urlAddress;
    private int id;
    private String status;
    private double latitude;
    private double longitude;
    private int caller_taking_id;
    private ProgressDialog pd;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmbulanceStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCaller_taking_id() {
        return caller_taking_id;
    }

    public void setCaller_taking_id(int caller_taking_id) {
        this.caller_taking_id = caller_taking_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Status change");
        pd.setMessage("Changing");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.send();
    }

    @Override
    protected void onPostExecute(String s) {
        pd.dismiss();

        if(s != null){
            Toast.makeText(context, "status changed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
        }
    }

    private String send() {
        // Connect
        HttpURLConnection con = Connector.httpUrlPostConnection(urlAddress);
        if(con == null){
            return null;
        }

        try {
            OutputStream os = con.getOutputStream();

            // Write
            BufferedWriter bw =  new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(new Ambulance(id, status, longitude, latitude, caller_taking_id).packData());

            // Release res
            bw.flush();
            bw.close();
            os.close();

            // Has it been success
            int responseCode = con.getResponseCode();

            if(responseCode == con.HTTP_OK){
                // Get response
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                String line = null;

                while ((line = br.readLine()) != null){
                    response.append(line);
                }

                // Release res
                br.close();

                return response.toString();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
