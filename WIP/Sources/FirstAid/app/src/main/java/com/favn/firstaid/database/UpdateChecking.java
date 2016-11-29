package com.favn.firstaid.database;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hung Gia on 11/27/2016.
 */

public class UpdateChecking extends AsyncTask<String, UpdateTable.Table, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String link = params[0];

        try {

            URL url = new URL(link);
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            UpdateTable updateTable = new Gson().fromJson(reader, UpdateTable.class);
            UpdateTable.Table[] updateTables = updateTable.getUpdate_table();

//            for (int i = 0; i < injuries.length; i++) {
//                Log.d("test_db", injuries[i].getId() + injuries[i].getInjury_name() +
//                        injuries[i].getSymptom() + injuries[i].getImage() + injuries[i]
//                        .getCreated_at() + injuries[i].getUpdated_at());
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
