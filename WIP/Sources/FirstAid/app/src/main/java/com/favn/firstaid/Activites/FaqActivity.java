package com.favn.firstaid.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.favn.firstaid.Adapter.FaqAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Faq;
import com.favn.firstaid.R;

import java.util.List;

public class FaqActivity extends AppCompatActivity {

    private FaqAdapter faqAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Faq> mFaqList;
    private Button btnQa;
    private int listSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final int injuryId = intent.getExtras().getInt("id");

        listView = (ListView) findViewById(R.id.list_faq);
        dbHelper = new DatabaseOpenHelper(this);

//        dbHelper.createDatabase();
//        dbHelper.openDatabase();

        mFaqList = dbHelper.getListFaq(injuryId);
        faqAdapter = new FaqAdapter(this, mFaqList);
        listView.setAdapter(faqAdapter);
        listSize = mFaqList.size();

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.instruction_detail_footer, null);
        btnQa = (Button) footerLayout.findViewById(R.id.button_faq);
        btnQa.setText("Đặt câu hỏi cho chúng tôi");
        btnQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, QAActivity.class);
                intent.putExtra("id", injuryId);
                startActivity(intent);
            }
        });

        listView.addFooterView(footerLayout);
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
