package com.favn.ambulance.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.ambulance.services.location.LocationChangeListener;
import com.favn.ambulance.services.location.LocationFinder;
import com.favn.ambulance.services.location.LocationStatus;
import com.favn.ambulance.commons.Ambulance;
import com.favn.ambulance.utils.Constants;
import com.favn.ambulance.utils.SharedPreferencesData;
import com.favn.ambulance.utils.NetworkStatus;
import com.favn.mikey.ambulance.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WaitingActivity extends AppCompatActivity implements LocationChangeListener {

    NotificationCompat.Builder notification;
    private static final int id = 45612;

    private Location mCurrentLocation;
    private LocationFinder locationFinder;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    private IntentFilter intentFilter;
    private boolean isReady;

    private Ambulance ambulance;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private Intent intent;

    private Switch swReady;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);

        //Get status swReady from LoginActivity and TaskActivity
try {
    isReady = intent.getExtras().getBoolean("isReady");
    Toast.makeText(this, isReady + "this is my Toast message!!! =)",
            Toast.LENGTH_LONG).show();
} catch(Exception e) {

}



        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        createLocationFinder();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);

        // Get ambulance info from SharedPreferences
        ambulance = SharedPreferencesData.getAmbulanceData(Constants.SPREFS_AMBULANCE_INFO_KEY);


        // TODO : HANDLE FIREBASE
        // Init instant firebase database - KienMT
//        database = FirebaseDatabase.getInstance();
        // Init firebase database reference
        //     DatabaseReference dbRef = database.getReference("ambulances");
//        dbRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Ambulance tmpAmbulanceChange = dataSnapshot.getValue(Ambulance.class);
//                if(tmpAmbulanceChange.getId() != ambulance.getId()) {
//                    return;
//                }
//
//
//                // Handle task
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference dbRef = database.getReference("ambulances/" + ambulance.getId());
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, String> data = dataSnapshot.get
//                Log.w("data_snapshot", dataSnapshot.toString());
//                Ambulance tmp = (Ambulance) dataSnapshot.getValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        updateAmbulance(Constants.STATUS_READY);
        //TODO : END HANDLE FIREBASE


        // createTaskDialog();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        swReady = (Switch) findViewById(R.id.switch_ready);

        if (isReady) {
            swReady.setChecked(true);
            setLayoutNotReadyUI(false);
        } else {
            swReady.setChecked(false);
            setLayoutNotReadyUI(true);
        }
        swReady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OnSwitch();
                } else {
                    OffSwitch();
                }
            }
        });

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
                notification.setContentTitle("Cấp cứu 115");
                notification.setContentText("Có nhiệm vụ");

                Intent intent = new Intent(this, TaskActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                //build noti and issues
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(id, notification.build());

                //show task dialog
//                createTaskDialog();

                createLogoutDialog();
                break;
            case R.id.application_info:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    // Broadcast for Connectivity status
    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only work when click on off button
            if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                isLocationEnable = LocationStatus.checkLocationProvider(context);

            } else if (intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                if (!isNetworkEnable) {
                    createNetworkSetting();
                }
                // Check location enable in connectivity change
                isLocationEnable = LocationStatus.checkLocationProvider(context);
            }

            locationFinder.connectGoogleApiClient();
        }
    };

    public void createLocationFinder() {
        locationFinder = new LocationFinder(this, this);
        locationFinder.buildLocationFinder();
        locationFinder.connectGoogleApiClient();
    }


    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(WaitingActivity.this, Constants.REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    @Override
    public void locationChangeSuccess(Location location) {
        mCurrentLocation = location;
        Log.d("location_test", location + "");

        // Need to check if ambulance != null
    }

    private void buildNetworkSetting() {
        isNetworkEnable = NetworkStatus.checkNetworkEnable(this);
        if (!isNetworkEnable) {
            createNetworkSetting();
        }
    }

    private void createNetworkSetting() {
        new AlertDialog.Builder(this)
                .setTitle("Kết nối Internet")
                .setMessage("Vào cài đặt Internet")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .create()
                .show();

    }

    private void createTaskDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Nhiệm vụ")
                .setMessage("Có nhiệm vụ")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        declineTask();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acceptTask();
                    }
                })
                .create()
                .show();
    }

    private void declineTask() {
        Toast.makeText(this, "Hủy rồi nhé !", Toast.LENGTH_LONG).show();
    }

    private void acceptTask() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("ambulances/" + ambulance.getId());
        dbRef.child("status").setValue("buzy");
    }

    //Create logout dialog
    private void createLogoutDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất khỏi ứng dụng không ?")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        Intent intent = new Intent(WaitingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WaitingActivity Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    // Handle update ambulance when change status - added by KienMT : 12/4/2016
    public void updateAmbulance(String type) {
        DatabaseReference dbRef = database.getReference("ambulances/" + ambulance.getId());
        if (type.equals(Constants.STATUS_READY)) {
            dbRef.child("caller_taking_id").setValue(null);
        }
        if (mCurrentLocation != null) {
            dbRef.child("latitude").setValue(mCurrentLocation.getLatitude());
            dbRef.child("longitude").setValue(mCurrentLocation.getLongitude());
        }
        dbRef.child("status").setValue(type);
    }

    //On switch swReady
    public void OnSwitch() {
        setLayoutNotReadyUI(false);
    }

    //Off switch swReady
    public void OffSwitch() {
        setLayoutNotReadyUI(true);
    }

    private void setLayoutNotReadyUI(boolean isNotReady) {
        LinearLayout llReadyNotReady = (LinearLayout) findViewById(R.id.layout_not_ready);
        TextView tvReadyStatus = (TextView) findViewById(R.id.textview_ready_status);
        if (!isNotReady) {
            llReadyNotReady.setAlpha(0);
            tvReadyStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            llReadyNotReady.setAlpha(1);
            tvReadyStatus.setTextColor(getResources().getColor(R.color.colorEditText));
        }
    }

}

