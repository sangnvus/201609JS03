package com.favn.firstaid.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.favn.firstaid.activites.BannerDetail;
import com.favn.firstaid.activites.InstructionDetail;
import com.favn.firstaid.adapter.InjuryAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.Models.Commons.Constants.LISTVIEW_EMERGENCY;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearningFragment extends Fragment implements AdapterView.OnItemClickListener {
    private InjuryAdapter adapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Injury> mInjuryList;
    public static final int FROM_LEARNING = 2;

    public LearningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_learning, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        dbHelper = new DatabaseOpenHelper(getActivity());
        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInjuryList = dbHelper.getListInjury();

        // TODO : pending data for learning screen
        // adapter = new InjuryAdapter(getActivity(), mInjuryList);
        adapter = new InjuryAdapter(getActivity(), mInjuryList, LISTVIEW_EMERGENCY);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        container.removeAllViews();

        FrameLayout headerLayout = (FrameLayout) inflater.inflate(R.layout.banner, null);
        ImageView banner = (ImageView) headerLayout.findViewById(R.id.image_banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerDetail.class);
                startActivity(intent);
            }
        });

        listView.addHeaderView(headerLayout);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Injury injury = (Injury) listView.getItemAtPosition(position);
        int injuryId = injury.getId();
        String injuryName = injury.getInjuryName();

        Intent intent = new Intent(getActivity(), InstructionDetail.class);
        intent.putExtra("id", injuryId);
        intent.putExtra("name", injuryName);
        intent.putExtra("typeOfAction", FROM_LEARNING);
        startActivity(intent);
    }

}
