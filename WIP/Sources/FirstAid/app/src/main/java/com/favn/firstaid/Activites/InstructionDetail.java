package com.favn.firstaid.Activites;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
