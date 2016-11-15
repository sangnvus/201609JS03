package com.favn.ambulance.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.favn.mikey.ambulance.R;

public class Information extends AppCompatActivity {

    NotificationCompat.Builder notification;
    private  static final int id = 45612;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    public void btnNotiClicked(View view){
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
    }
}
