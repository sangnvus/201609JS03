package com.favn.firstaid.Activites;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    int position = 0;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final int injuryId = intent.getExtras().getInt("id");
        String name = intent.getExtras().getString("name");
        int kind = intent.getExtras().getInt("kind");
        getSupportActionBar().setTitle(name);

        //toast check kind
        if(kind == 1){
            isEmegency = true;
//            Toast.makeText(getApplicationContext(), "Emergency instruction",
//                    Toast.LENGTH_LONG).show();
        } else {
            isEmegency = false;
//            Toast.makeText(getApplicationContext(), "Learning instruction",
//                    Toast.LENGTH_LONG).show();
        }

        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = new DatabaseOpenHelper(this);

        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInstructionList = dbHelper.getListInstruction(injuryId);
        instructionAdapter = new InstructionAdapter(this, mInstructionList, isEmegency);
        listView.setAdapter(instructionAdapter);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.instruction_detail_footer,null);
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

        final int[] audio = {R.raw.audio_1,R.raw.audio_2,R.raw.audio_3};

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Instruction instruction = (Instruction) listView.getItemAtPosition(position);
//                // ListView Clicked item index
//                int itemPosition     = position;
//
//                // ListView Clicked item value
//                cleanup();
//                mp = MediaPlayer.create(InstructionDetail.this, audio[position]);
//
//                mp.start();
//
//            }
//        });




        // Speak instruction step by step when click
        ImageView speak = (ImageView) findViewById(R.id.btn_speak);
        ImageView repeat = (ImageView) findViewById(R.id.btn_repeat);
        TextView next = (TextView) findViewById(R.id.btn_next);


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanup();
                mp = MediaPlayer.create(InstructionDetail.this, audio[position]);

                mp.start();

                position++;

            }
        });

    }

    public void cleanup() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
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
