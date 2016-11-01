package com.favn.firstaid.Fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.favn.firstaid.Activites.InstructionDetail;
import com.favn.firstaid.Activites.MapsActivity;
import com.favn.firstaid.Adapter.InjuryAdapter;
import com.favn.firstaid.Database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearningFragment extends Fragment implements AdapterView.OnItemClickListener {
    private InjuryAdapter adapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Injury> mInjuryList;
    final int kind = 2;

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
        adapter = new InjuryAdapter(getActivity(), mInjuryList);
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
        intent.putExtra("kind", kind);
        startActivity(intent);
    }

}
