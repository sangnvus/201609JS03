package com.favn.firstaid.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.favn.firstaid.Adapter.InjuryAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.Models.Common.Constant.SPINNER_INJURY;

public class QAActivity extends AppCompatActivity {

    // Declare all elements, controls and variables
    private InjuryAdapter injuryAdapter;
    private DatabaseOpenHelper dbHelper;
    private Spinner spnListInjury;
    private List<Injury> mInjuryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControl();

    }

    // Init elements, controls and variables
    private void addControl() {
        dbHelper = new DatabaseOpenHelper(this);
        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInjuryList = dbHelper.getListInjury();
        injuryAdapter = new InjuryAdapter(this, mInjuryList, SPINNER_INJURY);
        spnListInjury = (Spinner) findViewById(R.id.spin_injury);
        spnListInjury.setAdapter(injuryAdapter);

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
