package com.favn.firstaid.Activites;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

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
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        int injuryId = intent.getExtras().getInt("id");

        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = new DatabaseOpenHelper(this);

        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInstructionList = dbHelper.getListInstruction(injuryId);
        instructionAdapter = new InstructionAdapter(this, mInstructionList);
        listView.setAdapter(instructionAdapter);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.instruction_detail_footer,null);
        btnFaq = (Button) footerLayout.findViewById(R.id.button_faq);

        listView.addFooterView(footerLayout);


        // Speak instruction step by step when click
        FloatingActionButton fabSpeak = (FloatingActionButton) findViewById(R.id.fab_speak);
        fabSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(position);
                Log.d("listview", position + "");
                position++;
                if (position == mInstructionList.size()) {
                    position = 0;
                }
            }
        });

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
