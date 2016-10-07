package com.favn.firstaid.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.favn.firstaid.Adapter.InstructionAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
        int injuryId= intent.getExtras().getInt("id");
        Toast.makeText(this, injuryId + "", Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.listview_instruction);
        dbHelper = new DatabaseOpenHelper(this);

        File database = this.getApplicationContext().getDatabasePath(DatabaseOpenHelper.DB_PATH);
        if(database.exists() == false) {
            dbHelper.getReadableDatabase();
            copyDatabase(this);
        }
        mInstructionList = dbHelper.getListInstruction(injuryId);
        instructionAdapter = new InstructionAdapter(this, mInstructionList);
        listView.setAdapter(instructionAdapter);

    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseOpenHelper.DB_NAME);
            String outFileName = DatabaseOpenHelper.DB_PATH + DatabaseOpenHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
