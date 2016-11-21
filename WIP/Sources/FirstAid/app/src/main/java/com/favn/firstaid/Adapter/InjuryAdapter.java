package com.favn.firstaid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.models.Commons.Constants.LISTVIEW_EMERGENCY;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class InjuryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Injury> mInjuryList;
    private int controlType; // Add for check control type - by kienmt 11/04/2016
    private String icon_src;

    public InjuryAdapter(Context mContext, List<Injury> mInjuryList, int controlType) {
        this.mContext = mContext;
        this.mInjuryList = mInjuryList;
        this.controlType = controlType;
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
//        View v = View.inflate(mContext, R.layout.item_injury, null);
//        TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
//        txtView.setText(mInjuryList.get(position).getInjury_name());
//        return v;

        // Edit - Check control type to return view - by Kienmt 11/04/2016
        if (controlType == LISTVIEW_EMERGENCY){
            View v = View.inflate(mContext, R.layout.item_injury, null);
            TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
            ImageView icon = (ImageView) v.findViewById(R.id.image_injury_item);
            txtView.setText(mInjuryList.get(position).getInjury_name());
            icon_src = mInjuryList.get(position).getSymptom();

            int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                    mInjuryList.get(position).getSymptom(), null, null);
            icon.setImageResource(imagePath);

            return v;
        } else {
            View v = View.inflate(mContext, R.layout.spinner_layout, null);
            TextView txtInjuryName = (TextView) v.findViewById(R.id.text_injury_name);
            txtInjuryName.setText(mInjuryList.get(position).getInjury_name());
            return v;
        }
    }
}
