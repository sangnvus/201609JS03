package com.favn.firstaid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.favn.firstaid.adapter.InjuryAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.Models.Commons.Constants.SPINNER_INJURY;


/**
 * A simple {@link Fragment} subclass.
 */
public class QAFragment extends Fragment {

    // Declare all elements, controls and variables
    private TextView tvName;
    private TextView tvEmail;
    private RadioButton rdInjury;
    private Spinner spnListInjury;
    private RadioButton rdTitle;
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnSend;
    private InjuryAdapter injuryAdapter;
    private DatabaseOpenHelper dbHelper;
    private List<Injury> mInjuryList;


    public QAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_qa, container, false);

        // Call function to init control
        addControl(rootView);

        container.removeAllViews();
        return rootView;
    }

    // Init elements, controls and variables
    private void addControl(ViewGroup rootView) {


        dbHelper = new DatabaseOpenHelper(getActivity());
        dbHelper.createDatabase();
        dbHelper.openDatabase();

        mInjuryList = dbHelper.getListInjury();
        injuryAdapter = new InjuryAdapter(getActivity(), mInjuryList, SPINNER_INJURY);
        spnListInjury = (Spinner) rootView.findViewById(R.id.spin_injury);
        spnListInjury.setAdapter(injuryAdapter);

    }

}
