package com.favn.ambulance.services;

import android.os.AsyncTask;

import com.favn.ambulance.commons.Caller;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hung Gia on 12/17/2016.
 */

public class CallerInformationGetter {
    private CallerInformationGetterListener callerInformationGetterListener;
    private int ambulanceId;
    DownloadRawData downloadRawData;

    public CallerInformationGetter(CallerInformationGetterListener
                                           callerInformationGetterListener, int ambulanceId) {
        this.callerInformationGetterListener = callerInformationGetterListener;
        this.ambulanceId = ambulanceId;
    }

    public void execute() throws UnsupportedEncodingException {
        downloadRawData = new DownloadRawData();
        String url = "http://dispatcher.rtsvietnam.com/getcallerbyambulance/" + ambulanceId;
        downloadRawData.execute(url);
    }

    public void stop() {
        downloadRawData.cancel(true);
    }

    private class DownloadRawData extends AsyncTask<String, Void, Caller> {
        @Override
        protected Caller doInBackground(String... params) {
            String link = params[0];
            Caller caller = null;
            try {
                URL url = new URL(link);
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
                caller = new Gson().fromJson(reader, Caller.class);


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

            return caller;
        }

        @Override
        protected void onPostExecute(Caller caller) {
            try {
                callerInformationGetterListener.getCallerInformationSuccess(caller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
