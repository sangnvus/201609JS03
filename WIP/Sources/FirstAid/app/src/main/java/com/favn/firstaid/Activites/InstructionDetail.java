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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.favn.firstaid.R;
import com.favn.firstaid.adapters.InstructionAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;
import com.favn.firstaid.services.location.LocationChangeListener;
import com.favn.firstaid.services.location.LocationFinder;
import com.favn.firstaid.services.location.LocationStatus;
import com.favn.firstaid.models.CallerInfoSender;
import com.favn.firstaid.utils.Constants;
import com.favn.firstaid.models.InformationSenderListener;
import com.favn.firstaid.utils.NetworkStatus;
import com.favn.firstaid.utils.SOSCalling;
import com.favn.firstaid.utils.SettingPref;
import com.favn.firstaid.models.Instruction;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class InstructionDetail extends AppCompatActivity implements LocationChangeListener,
        InstructionAdapter.InformationSending, InformationSenderListener {
    private InstructionAdapter instructionAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Instruction> mInstructionList;
    private Button btnFaq;
    private boolean isEmergency;
    private int playingAudioId;
    private MediaPlayer mMediaPlayer = null;
    private boolean isAllowedSendInformation;
    private String  phoneNumber;
    private IntentFilter intentFilter;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    BroadcastReceiver connectivityBroadcastReceiver;
    private LocationFinder locationFinder;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean isCalled;
    //add this
    private Location mCurrentLocation;
    private boolean isSentUserInfo;

    // Web service url, get caller info from app - KienMT : 11/24/2016
    private String urlAddress;

    private LinearLayout llSendingStatus;
    private TextView tvSendingInformationStatus;

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

//        loadPref();
        Intent intent = getIntent();

        // START EDIT : Declare injuryID in class level -> others function can access
        // final int injuryId = intent.getExtras().getInt("id");
        injuryId = intent.getExtras().getInt("id");

        // Assign url address value (web service url) - Kienmt : 11/24/2016
        urlAddress = "http://104.199.149.193/caller";

        String name = intent.getExtras().getString("name");
        int typeOfAction = intent.getExtras().getInt("typeOfAction");
        getSupportActionBar().setTitle(name);


        isEmergency = (typeOfAction == 1) ? true : false;
        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = DatabaseOpenHelper.getInstance(this);
        isLocationEnable = false;
        isNetworkEnable = false;

        //TODO Get value isAllowSendInformation from SharePreference
        phoneNumber = SettingPref.loadPhoneNumber(this);

        isAllowedSendInformation = (phoneNumber != null) ? true : false;

        Log.d("phone_number", phoneNumber + " - " + isAllowedSendInformation);


        if (isEmergency) {
            mInstructionList = dbHelper.getListInstruction(injuryId, isEmergency);
            instructionAdapter = new InstructionAdapter(this, this, mInstructionList, isEmergency);
            listView.setAdapter(instructionAdapter);
            locationFinder = new LocationFinder(this, this);
            if (isAllowedSendInformation) {
                createBroadcast();
                intentFilter = new IntentFilter();
                intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
                intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
                isCalled = false;
                // add this
                isSentUserInfo = false;
            }

            final int[] audio = {R.raw.audio_1, R.raw.audio_2, R.raw.audio_3};

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                    if (playingAudioId == audio[pos] && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
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
        } else {
            mInstructionList = dbHelper.getListInstruction(injuryId, isEmergency);
            instructionAdapter = new InstructionAdapter(this, this, mInstructionList, isEmergency);
            listView.setAdapter(instructionAdapter);

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

        //TODO send information to server

        //sendCallerInfoToServer(location);

        // START SEND TO FIREBAE
        // Init caller
        // Caller caller = new Caller();
        // caller.setPhone("01694639816");
        // caller.setInjuryId(injuryId);
        // caller.setLatitude(location.getLatitude());
        // caller.setLongitude(location.getLongitude());
        // caller.setStatus("waiting");
        // mDb = FirebaseDatabase.getInstance().getReference();
        // mDb.child("callers").push().setValue(caller);
        // END SEND TO FIREBASE


        //add this
        // Set location to mCurrentLocation
        mCurrentLocation = location;
        if(isNetworkEnable && mCurrentLocation != null && !isSentUserInfo) {
            //TODO Send caller info when having network and location
            sendCallerInfoToServer(mCurrentLocation);
        }
    }

    // Send caller infor to db server - KienMT : 11/24/2016
    private void sendCallerInfoToServer(Location location) {
        CallerInfoSender ciSender = new CallerInfoSender();

        // Assign values
        ciSender.setContext(InstructionDetail.this);
        ciSender.setUrlAddress(urlAddress);
        ciSender.setPhone("0906111222");
        ciSender.setInjuryId(injuryId);
        ciSender.setLatitude(location.getLatitude());
        ciSender.setLongitude(location.getLongitude());
        ciSender.setStatus("waiting");
        ciSender.setInformationSenderListener(InstructionDetail.this);

        ciSender.execute();
    }


    // call button trigger this function
    @Override
    public void requestInformationSending() {
        isSentUserInfo = false;
        if (isAllowedSendInformation) {
            if (!isLocationEnable || !isNetworkEnable) {
                createDialog();
            } else {
                locationFinder.buildLocationFinder();
                locationFinder.connectGoogleApiClient();
                isCalled = true;
                createSendingInformationUI(true);
                updateSendingInformationUI();
                SOSCalling.makeSOSCall(this);
            }
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
                if (isCalled) {
                    updateSendingInformationUI();
                }
            }
        };

        //add this
        if(isNetworkEnable && mCurrentLocation != null && !isSentUserInfo) {
            //TODO Send caller info when having network and location
            sendCallerInfoToServer(mCurrentLocation);
        }

    }

    private void createDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gửi vị trí")
                .setMessage("Chức năng gửi vị trí cần internet và gps")
                .setNegativeButton("GỌI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start call 115
                        SOSCalling.makeSOSCall(getBaseContext());
                    }
                })
                .setPositiveButton("CÀI ĐẶT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationFinder.buildLocationFinder();
                        locationFinder.connectGoogleApiClient();
                        if (!NetworkStatus.checkNetworkEnable(getBaseContext())) {
                            createNetworkSetting();
                        }
                        isCalled = true;
                        createSendingInformationUI(true);
                        updateSendingInformationUI();
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

    private void createSendingInformationUI(boolean isShow) {
        llSendingStatus = (LinearLayout) findViewById(R.id.layout_sending_status);

        if (isShow) {
            llSendingStatus.setVisibility(View.VISIBLE);
            tvSendingInformationStatus = (TextView) findViewById(R.id
                    .textview_sending_information_status);
        } else {
            llSendingStatus.setVisibility(View.GONE);
        }
    }

    private void updateSendingInformationUI() {
        if (isLocationEnable && isNetworkEnable) {
            tvSendingInformationStatus.setText(Constants.INFO_ENABLE_CONNECTIVITY);
            llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
        } else {
            tvSendingInformationStatus.setText(Constants.INFO_WARNING_CONNECTIVITY);
            llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorWarning));

        }
    }

    @Override
    public void sendInformationListener(String sendingStatus) {
        switch (sendingStatus) {
            case Constants.INFO_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorProcessing));
                break;
            case Constants.INFO_SUCCESS_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_SUCCESS_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                isSentUserInfo = true;
                break;
            case Constants.INFO_ERROR_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_ERROR_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorWarning));
                break;
        }
    }


}
