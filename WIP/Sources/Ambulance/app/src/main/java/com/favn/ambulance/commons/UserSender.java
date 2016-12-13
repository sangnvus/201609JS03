package com.favn.ambulance.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.favn.ambulance.activities.WaitingActivity;
import com.favn.ambulance.utils.Constants;
import com.favn.ambulance.utils.SharedPreferencesData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by KienMT on 11/16/2016.
 */

public class UserSender extends AsyncTask<Void, Void, String>{
    private Context context;
    private String urlAddress;
    private String username;
    private String password;
    private ProgressDialog pd;
    private User user;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        pd.setTitle("Đăng nhập");
        pd.setMessage("Đăng nhập...");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.send();
    }

    @Override
    protected void onPostExecute(String user) {
        pd.dismiss();

        if(user == null){
            Toast.makeText(context, "Lỗi kết nối !", Toast.LENGTH_LONG).show();
        } else if(user.equals("[\"wrong\"]")){
            Toast.makeText(context, "Sai tên đăng nhập hoặc mật khẩu !", Toast.LENGTH_LONG).show();
        } else if(user.equals("[\"accessdenied\"]")) {
            Toast.makeText(context, "Tài khoản không có quyền đăng nhập !", Toast.LENGTH_LONG).show();
        } else {
            goToWaitingActivity(true); //extract to method 10/12
            SharedPreferencesData.saveData(context, Constants.SPREFS_NAME, Constants
                    .SPREFS_AMBULANCE_INFO_KEY, user);
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
            user = new User(username, password);
            bw.write(user.packData());

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


    // create this method 10/12
    private void goToWaitingActivity(boolean status) {
        Intent intent = new Intent(context, WaitingActivity.class);
        intent.putExtra("isReady", status);
        context.startActivity(intent);
    }


}
