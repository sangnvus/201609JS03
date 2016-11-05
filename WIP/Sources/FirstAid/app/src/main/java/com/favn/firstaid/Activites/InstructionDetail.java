package com.favn.firstaid.Activites;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.favn.firstaid.Adapter.InstructionAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

import java.util.List;

public class InstructionDetail extends AppCompatActivity {
    private InstructionAdapter instructionAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Instruction> mInstructionList;
    private Button btnFaq;
    private boolean isEmegency;
    private int playingAudioId;
    private MediaPlayer mMediaPlayer = null;

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

        isEmegency = (typeOfAction == 1) ? true : false;

        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = new DatabaseOpenHelper(this);

        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInstructionList = dbHelper.getListInstruction(injuryId);
        instructionAdapter = new InstructionAdapter(this, mInstructionList, isEmegency);
        listView.setAdapter(instructionAdapter);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        final int[] audio = {R.raw.audio_1, R.raw.audio_2, R.raw.audio_3};

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                final TextView stepAnimation = (TextView) view.findViewById(R.id.text_step_number);
                if (playingAudioId == audio[pos] && mMediaPlayer.isPlaying() == true) {
                    mMediaPlayer.stop();
                    stepAnimation.clearAnimation();
                } else {
                    playAudio(audio[pos], stepAnimation);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaPlayer!=null) {
            mMediaPlayer.stop();
        }
    }

    private void playAudio(int audioId, TextView stepAnim) {
        playingAudioId = audioId;
        Animation beat = AnimationUtils.loadAnimation(this, R.anim.heartbeat);
        // stop the previous playing audio
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && stepAnim.isEnabled()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            stepAnim.clearAnimation();
        }
        mMediaPlayer = MediaPlayer.create(this, audioId);
        mMediaPlayer.start();
        stepAnim.startAnimation(beat);
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
}
