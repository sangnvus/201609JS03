package com.favn.ambulance.models.Commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by KienMT on 11/28/2016.
 */

public class AmbulanceInfoSender extends AsyncTask<Void, Void, String>{
    Context context;
    String urlAddress;
    String phone;
    int injuryId;
    double latitude;
    double longitude;
    String status;
    ProgressDialog pd;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
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

    public String getAmbulanceStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProgressDialog getPd() {
        return pd;
    }

    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }
}
