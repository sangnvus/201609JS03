package com.favn.firstaid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.favn.firstaid.Models.HealthFacility;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Hung Gia on 10/21/2016.
 */

public class HealthFacilityAdapter extends BaseAdapter{
    private Context mContext;
    private List<HealthFacility> mHealthFacilityList;

    public HealthFacilityAdapter(Context mContext, List<HealthFacility> mHealthFacilityList) {
        this.mContext = mContext;
        this.mHealthFacilityList = mHealthFacilityList;
    }

    @Override
    public int getCount() {
        return mHealthFacilityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHealthFacilityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_hospital, null);
        HealthFacility healthFacility = mHealthFacilityList.get(position);

        TextView tvHealthFacilityName  = (TextView)v.findViewById(R.id.text_health_facility_name);
        tvHealthFacilityName.setText(healthFacility.getName());

        TextView tvHealthFacilityAddress = (TextView) v.findViewById(R.id.text_health_facility_address);

        String address = (healthFacility.getAddress() != null) ? healthFacility.getAddress() :
                "Ở khu vực " + healthFacility.getVicinity();
        tvHealthFacilityAddress.setText(address);

        if (healthFacility.getDistance() != null) {
            TextView hospitalDistance = (TextView) v.findViewById(R.id.text_hospital_distance);
            hospitalDistance.setText(mHealthFacilityList.get(position).getDistance().getText());
        }
        return v;
    }
}
