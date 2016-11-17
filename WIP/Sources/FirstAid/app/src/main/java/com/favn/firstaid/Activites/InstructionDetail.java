package com.favn.firstaid.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.favn.firstaid.Adapter.InstructionAdapter;
import com.favn.firstaid.Adapter.LearningInstructionAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.Models.LearningInstruction;
import com.favn.firstaid.R;

import java.util.List;

public class InstructionDetail extends AppCompatActivity {
    private InstructionAdapter instructionAdapter;
    private LearningInstructionAdapter LearningInstructionAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Instruction> mInstructionList;
    private List<LearningInstruction> mLearningInstructionList;
    private Button btnFaq;
    private boolean isEmegency;
    private int playingAudioId;
    private MediaPlayer mMediaPlayer = null;
    private int listSize;

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

        if(isEmegency) {
            mInstructionList = dbHelper.getListInstruction(injuryId);
            instructionAdapter = new InstructionAdapter(this, mInstructionList, isEmegency);
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
                        callIntent.setData(Uri.parse("tel:115"));
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaPlayer!=null) {
            mMediaPlayer.stop();
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
}
