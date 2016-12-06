package com.favn.firstaid.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.favn.firstaid.utils.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


/**
 * Created by MeHaDake on 11/5/2016.
 */

public class QuestionSender extends AsyncTask<Void, Void, String> {
    Context context;
    String urlAddress;
    String injury_id;
    String asker;
    String asker_email;
    String title;
    String content;
    ProgressDialog pd;

    public QuestionSender() {
        String urlAddress = "";
        String injury_id = "";
        String asker = "";
        String asker_email = "";
        String title = "";
        String content = "";
    }

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

    public String getInjury_id() {
        return injury_id;
    }

    public void setInjury_id(String injury_id) {
        this.injury_id = injury_id;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public String getAsker_email() {
        return asker_email;
    }

    public void setAsker_email(String asker_email) {
        this.asker_email = asker_email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Gửi");
        pd.setMessage("Đang gửi câu hỏi...");
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
            Toast.makeText(context, "Gửi câu hỏi thành công", Toast.LENGTH_LONG).show();
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
            bw.write(new Question(injury_id, asker, asker_email, title, content).packData());

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
