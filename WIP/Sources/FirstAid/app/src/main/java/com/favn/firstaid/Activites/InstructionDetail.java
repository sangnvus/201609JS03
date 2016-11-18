package com.favn.firstaid.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.favn.firstaid.locationUtil.LocationStatus;
import com.favn.firstaid.models.Commons.Constants;
import com.favn.firstaid.models.Commons.NetworkStatus;
import com.favn.firstaid.models.Instruction;
import com.favn.firstaid.models.LearningInstruction;
import com.favn.firstaid.R;
import com.google.android.gms.common.api.Status;

import java.util.List;

public class InstructionDetail extends AppCompatActivity implements LocationChangeListener {
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
    private TextView tvNotifySendFunctionality;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        final int injuryId = intent.getExtras().getInt("id");
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
            instructionAdapter = new InstructionAdapter(this, mInstructionList, isEmergency, isAllowedSendInformation);
            listView.setAdapter(instructionAdapter);

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
                Button call115 = (Button) view.findViewById(R.id.button_call);
                call115.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(Constants.CALL_115));

                    }
                });

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


        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
        intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
        updateNotifyStatus();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopLocationUpdate();
//                Log.d("onLocationChanged", "stop");
//            }
//        }, 20000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        unregisterReceiver(connectivityBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, intentFilter);
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
                }
                // Check location enable in connectivity change
                isLocationEnable = LocationStatus.checkLocationProvider(context);
            }
            updateNotifyStatus();
        }
    };

    private void updateNotifyStatus() {
        int callButtonPosition = -1;
        for (int i = 0; i < mInstructionList.size(); i++) {
            if (mInstructionList.get(i).isMakeCall()) {
                callButtonPosition = i;
            }
        }

        View view = listView.getChildAt(callButtonPosition);
        if (view != null) {
            tvNotifySendFunctionality = (TextView) view.findViewById(R.id
                    .textview_notify_send_information);

            if (tvNotifySendFunctionality != null) {
                if (isNetworkEnable && isLocationEnable) {
                    tvNotifySendFunctionality.setText(Constants.ENABLE_CONNECTIVITY);
                    tvNotifySendFunctionality.setTextColor(getResources().getColor(R.color
                            .colorNavigate));
                } else {
                    tvNotifySendFunctionality.setText(Constants.WARNING_CONNECTIVITY);
                    tvNotifySendFunctionality.setTextColor(getResources().getColor(R.color
                            .colorPrimary));

                }
            }
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
    }

    @Override
    public void locationChangeSuccess(Location location) {

        Log.d("location_test", location + "");
        mCurrentLocation = location;


        // Send data to Server

        // Stop location update


    }

    private void stopLocationUpdate() {
        instructionAdapter.stopLocationUpdate();
    }

}
