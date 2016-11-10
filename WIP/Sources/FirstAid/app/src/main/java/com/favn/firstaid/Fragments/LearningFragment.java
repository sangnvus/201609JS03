package com.favn.firstaid.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.favn.firstaid.Activites.InstructionDetail;
import com.favn.firstaid.Adapter.InjuryAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
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
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Injury injury = (Injury) listView.getItemAtPosition(position);
        int injuryId = injury.getId();

        Intent intent = new Intent(getActivity(), InstructionDetail.class);
        intent.putExtra("id", injuryId);
        intent.putExtra("typeOfAction", FROM_LEARNING);
        startActivity(intent);
    }

}
