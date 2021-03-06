package com.favn.firstaid.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.commons.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.utils.Constants.LISTVIEW_EMERGENCY;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class InjuryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Injury> mInjuryList;
    private int controlType; // Add for check control type - by kienmt 11/04/2016
    private String icon_src;
    private boolean isSearching;

    public InjuryAdapter(Context mContext, List<Injury> mInjuryList, int controlType) {
        this.mContext = mContext;
        this.mInjuryList = mInjuryList;
        this.controlType = controlType;
        this.isSearching = isSearching;
    }

    public InjuryAdapter(Context mContext, List<Injury> mInjuryList, int controlType, boolean isSearching) {
        this.mContext = mContext;
        this.mInjuryList = mInjuryList;
        this.controlType = controlType;
        this.isSearching = isSearching;
    }

    @Override
    public int getCount() {
        return mInjuryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInjuryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mInjuryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Injury injury = mInjuryList.get(position);

        // Edit - Check control type to return view - by Kienmt 11/04/2016
        if (controlType == LISTVIEW_EMERGENCY){
            View v = View.inflate(mContext, R.layout.item_injury, null);
            TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
            ImageView icon = (ImageView) v.findViewById(R.id.image_injury_item);
            txtView.setText(injury.getInjury_name());
            icon_src = injury.getImage();

            int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                    injury.getImage(), null, null);
            icon.setImageResource(imagePath);

            if(isSearching && !injury.getSymptom().isEmpty()) {
                TextView tvSymptom = (TextView) v.findViewById(R.id.textview_injury_symptom);
                tvSymptom.setVisibility(View.VISIBLE);
                tvSymptom.setText("Triệu chứng: " + injury.getSymptom());
            } else {
                TextView tvSymptom = (TextView) v.findViewById(R.id.textview_injury_symptom);
                tvSymptom.setVisibility(View.GONE);
            }

            return v;
        } else {
            View v = View.inflate(mContext, R.layout.spinner_layout, null);
            TextView txtInjuryName = (TextView) v.findViewById(R.id.text_injury_name);
            txtInjuryName.setText(injury.getInjury_name());
            return v;
        }
    }
}
