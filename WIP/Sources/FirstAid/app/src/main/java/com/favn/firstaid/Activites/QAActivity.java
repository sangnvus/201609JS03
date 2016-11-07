package com.favn.firstaid.Activites;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.firstaid.Adapter.InjuryAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Common.QuestionSender;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.Models.Common.Constant.SPINNER_INJURY;

public class QAActivity extends AppCompatActivity {

    // Declare all elements, controls and variables
    private EditText etName;
    private EditText etEmail;
    private RadioButton rdInjury;
    private Spinner spnListInjury;
    private RadioButton rdTitle;
    private EditText etTittle;
    private EditText etContent;
    private Button btnSend;
    private InjuryAdapter injuryAdapter;
    private DatabaseOpenHelper dbHelper;
    private List<Injury> mInjuryList;
    private String urlAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initControl();
        setControlDefaultStatus();


    }

    // Init elements, controls and variables
    private void initControl() {
        urlAddress = "http://10.20.19.77:8080/Captons_Project/Source/WIP/Sources/FAVN_web/public/question";

        // Init dbHelper object
        dbHelper = new DatabaseOpenHelper(this);
        dbHelper.createDatabase();
        dbHelper.openDatabase();

        // Init screen control
        etName = (EditText) findViewById(R.id.edittext_name);
        etEmail = (EditText) findViewById(R.id.edittext_email);
        rdInjury = (RadioButton)  findViewById(R.id.radio_injury);
        rdTitle = (RadioButton)  findViewById(R.id.radio_title);
        spnListInjury = (Spinner) findViewById(R.id.spin_injury);
        etTittle = (EditText) findViewById(R.id.edittext_title);
        etContent = (EditText) findViewById(R.id.edittext_content);
        btnSend = (Button) findViewById(R.id.button_send);

        // Binding data to spiner
        mInjuryList = dbHelper.getListInjury();
        injuryAdapter = new InjuryAdapter(this, mInjuryList, SPINNER_INJURY);
        spnListInjury.setAdapter(injuryAdapter);

        // Set on checked listen radio injury
        rdInjury.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rdInjury.isChecked() == true) {
                    spnListInjury.setEnabled(true);
                } else {
                    spnListInjury.setEnabled(false);
                }
            }
        });

        // Set on checked listen radio title
        rdTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rdTitle.isChecked() == true) {
                    etTittle.setEnabled(true);
                } else {
                    etTittle.setEnabled(false);
                }
            }
        });

        // Set on send button click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assign values
                String asker = etName.getText().toString();
                String asker_email = etEmail.getText().toString();
                String content = etContent.getText().toString();

                String injuryID;
                String title;
                if (rdInjury.isChecked() == true) {
                    injuryID = String.valueOf(mInjuryList.get(spnListInjury.getSelectedItemPosition()).getId());
                    title = "";
                } else {
                    title = etTittle.getText().toString();
                    injuryID = "";
                }

                QuestionSender qs = new QuestionSender();
                qs.setContext(QAActivity.this);
                qs.setUrlAddress(urlAddress);
                qs.setAsker(asker);
                qs.setAsker_email(asker_email);
                qs.setInjuryID(injuryID);
                qs.setTitle(title);
                qs.execute();
            }
        });

    }

    // Disable control
    private void setControlDefaultStatus() {
        rdTitle.setChecked(false);
        etTittle.setEnabled(false);
        rdInjury.setChecked(true);
        spnListInjury.setEnabled(true);
    }

}
