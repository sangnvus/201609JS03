package com.favn.firstaid.models.Commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.favn.firstaid.models.Caller;
import com.favn.firstaid.models.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by KienMT on 11/18/2016.
 */

public class CallerInfoSender extends AsyncTask<Void, Void, String>{
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

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public void setContext(Context context) {
        this.context = context;
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

    public String getCallerStatus() {
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
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Gửi");
        pd.setMessage("Đang gửi thông tin tới 115");
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
            Toast.makeText(context, "Gửi thành công", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Chửa gửi được !", Toast.LENGTH_LONG).show();
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
            bw.write(new Caller(phone, injuryId, latitude, longitude, status).packData());

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
