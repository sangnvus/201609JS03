package com.favn.firstaid.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.firstaid.adapter.LearningInstructionAdapter;
import com.favn.firstaid.adapter.InstructionAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;

import com.favn.firstaid.locationUtil.LocationChangeListener;
import com.favn.firstaid.locationUtil.LocationFinder;
import com.favn.firstaid.locationUtil.LocationStatus;
import com.favn.firstaid.models.Caller;
import com.favn.firstaid.models.Commons.Constants;
import com.favn.firstaid.models.Commons.NetworkStatus;
import com.favn.firstaid.models.Commons.SOSCalling;
import com.favn.firstaid.models.Instruction;
import com.favn.firstaid.models.LearningInstruction;
import com.favn.firstaid.R;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class InstructionDetail extends AppCompatActivity implements LocationChangeListener,
        InstructionAdapter.InformationSending {
    private InstructionAdapter instructionAdapter;
    private LearningInstructionAdapter LearningInstructionAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Instruction> mInstructionList;
    private List<LearningInstruction> mLearningInstructionList;
    private Button btnFaq;
    private boolean isEmergency;
    private int playingAudioId;
    private MediaPlayer mMediaPlayer = null;
    private boolean isAllowedSendInformation;
    private IntentFilter intentFilter;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    private Location mCurrentLocation;
    BroadcastReceiver connectivityBroadcastReceiver;
    private LocationFinder locationFinder;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    // Declare firebase database reference - KienMT : 11/21/2016
    DatabaseReference mDb;
    // Move injuryId to class level
    private int injuryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        // START EDIT : Declare injuryID in class level -> others function can access
        // final int injuryId = intent.getExtras().getInt("id");
        injuryId = intent.getExtras().getInt("id");

        String name = intent.getExtras().getString("name");
        int typeOfAction = intent.getExtras().getInt("typeOfAction");
        getSupportActionBar().setTitle(name);


        isEmergency = (typeOfAction == 1) ? true : false;
        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = new DatabaseOpenHelper(this);
        isLocationEnable = false;
        isNetworkEnable = false;

        //TODO Get value isAllowSendInformation from SharePreference
        isAllowedSendInformation = true;


        if (isEmergency) {
            mInstructionList = dbHelper.getListInstruction(injuryId);
            instructionAdapter = new InstructionAdapter(this, this, mInstructionList, isEmergency);
            listView.setAdapter(instructionAdapter);
            locationFinder = new LocationFinder(this, this);
            if(isAllowedSendInformation) {
                createBroadcast();
                intentFilter = new IntentFilter();
                intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
                intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
            }
        } else {
            mLearningInstructionList = dbHelper.getListLearingInstruction(injuryId);
            LearningInstructionAdapter = new LearningInstructionAdapter(this, mLearningInstructionList);
            listView.setAdapter(LearningInstructionAdapter);

            FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.instruction_detail_footer, null);
            btnFaq = (Button) footerLayout.findViewById(R.id.button_faq);
            btnFaq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InstructionDetail.this, FaqActivity.class);
                    intent.putExtra("id", injuryId);
                    startActivity(intent);
                }
            });

            listView.addFooterView(footerLayout);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final int[] audio = {R.raw.audio_1, R.raw.audio_2, R.raw.audio_3};

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                if (playingAudioId == audio[pos] && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
//                    colorLine.setVisibility(View.INVISIBLE);
                } else {
                    playAudio(audio[pos]);

                }

                for (int i = 0; i < listView.getChildCount(); i++) {
                    if (pos == i) {
                        listView.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.list_item_color));
                    } else {
                        listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }


            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (connectivityBroadcastReceiver != null) {
            unregisterReceiver(connectivityBroadcastReceiver);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectivityBroadcastReceiver != null) {
            registerReceiver(connectivityBroadcastReceiver, intentFilter);
        }
    }


    private void playAudio(int audioId) {
        playingAudioId = audioId;

        // stop the previous playing audio
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = MediaPlayer.create(this, audioId);
        mMediaPlayer.start();
//        colorLine.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(InstructionDetail.this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    @Override
    public void locationChangeSuccess(Location location) {

        Log.d("location_test", location + "");
        mCurrentLocation = location;

        //TODO send information to server
        // Init caller
        Caller caller = new Caller();
        caller.setPhone("01694639816");
        caller.setInjuryId(injuryId);
        caller.setLatitude(location.getLatitude());
        caller.setLongitude(location.getLongitude());

        mDb = FirebaseDatabase.getInstance().getReference();
        mDb.child("callers").push().setValue(caller);

    }


    @Override
    public void requestInformationSending() {
        if (isAllowedSendInformation) {
            createDialog();
        } else {
            SOSCalling.makeSOSCall(this);
        }

    }

    private void createBroadcast() {
        // Broadcast for Connectivity status
        connectivityBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Only work when click on off button
                if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                    isLocationEnable = LocationStatus.checkLocationProvider(context);

                } else if (intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                    isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                    // Check location enable in connectivity change
                    isLocationEnable = LocationStatus.checkLocationProvider(context);
                }

            }
        };
    }

    private void createDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gửi vị trí")
                .setMessage("Chức năng gửi vị trí cần internet và gps")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start call 115
                        SOSCalling.makeSOSCall(getBaseContext());
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationFinder.buildLocationFinder();
                        locationFinder.connectGoogleApiClient();
                        if(!NetworkStatus.checkNetworkEnable(getBaseContext())) {
                            createNetworkSetting();
                        }

                    }
                })
                .create()
                .show();
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

}
