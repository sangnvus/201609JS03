package com.favn.ambulance.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.favn.mikey.ambulance.R;

public class WaitingScreen extends AppCompatActivity {

    NotificationCompat.Builder notification;
    private  static final int id = 45612;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.waiting_screen_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.sign_out:
                //build the noti
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setTicker("ticker");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("title");
                notification.setContentText("abcdef");

                Intent intent = new Intent(this, Task.class);
                PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                //build noti and issues
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(id, notification.build());
                break;
            case R.id.application_info:
                //TODO move to about acivity
                startActivity(new Intent(this, InformationScreen.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}


