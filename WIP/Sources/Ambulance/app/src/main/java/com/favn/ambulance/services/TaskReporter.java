package com.favn.ambulance.services;

import android.location.Location;
import android.os.AsyncTask;

import com.favn.ambulance.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 12/16/2016.
 */

public class TaskReporter {

    public void declineTask(int ambulanceID) {
        // update ambulance to database by webservice
        CallService callService = new CallService();
        String url = "http://dispatcher.rtsvietnam.com/declinetask/" + ambulanceID;
        callService.execute(url);

        // update ambulance to firebase
        //updateFbWhenReportTask(Constants.REPORT_TASK_DECLINE, ambulanceID);

    }



    public void readyToDoTask(int ambulanceID) {
        // update ambulance to database by webservice
        CallService callService = new CallService();
        String url = "http://dispatcher.rtsvietnam.com/readytodotask/" + ambulanceID;
        callService.execute(url);

    }

    public void acceptTask(int ambulanceID) {
        // update ambulance to database by webservice
        CallService callService = new CallService();
        String url = "http://dispatcher.rtsvietnam.com/accepttask/" + ambulanceID;
        callService.execute(url);
    }

    public void sendLocation(int ambulanceID, Location location) {
        //TODO send location to server
        CallService callService = new CallService();
        String url = "http://dispatcher.rtsvietnam.com/sendlocation/" + ambulanceID + "/" + location.getLatitude() + "/" + location.getLongitude();
        callService.execute(url);
    }

    private class CallService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
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

        }
    }





    public void updateFbWhenReportTask(String type, int ambulanceID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("ambulances/" + ambulanceID);
        if(type.equals(Constants.REPORT_TASK_DECLINE)) {
            dbRef.child("caller_taking_id").setValue(null);
            dbRef.child("status").setValue(Constants.AMBULANCE_STATUS_PROBLEM);
        }
    }

}
