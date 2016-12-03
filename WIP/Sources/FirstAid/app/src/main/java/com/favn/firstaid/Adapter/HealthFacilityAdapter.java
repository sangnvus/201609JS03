package com.favn.firstaid.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.favn.firstaid.models.HealthFacility;
import com.favn.firstaid.models.HealthFacilityFilter;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Hung Gia on 10/21/2016.
 */

public class HealthFacilityAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<HealthFacility> mHealthFacilityList;
    private HealthFacilityFilter healthFacilityFilter;

    public HealthFacilityAdapter(Context mContext, List<HealthFacility> healthFacilityList) {
        this.mContext = mContext;
        this.mHealthFacilityList = healthFacilityList;
    }

    public List<HealthFacility> getmHealthFacilityList() {
        return mHealthFacilityList;
    }

    public void setmHealthFacilityList(List<HealthFacility> mHealthFacilityList) {
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
        final HealthFacility healthFacility = mHealthFacilityList.get(position);

        TextView tvHealthFacilityName = (TextView) v.findViewById(R.id.text_health_facility_name);
        tvHealthFacilityName.setText(healthFacility.getName());

        TextView tvHealthFacilityAddress = (TextView) v.findViewById(R.id.text_health_facility_address);

        String address = (healthFacility.getAddress() != null) ? healthFacility.getAddress() : healthFacility.getVicinity();
        tvHealthFacilityAddress.setText(address);

        final String phoneNumber = healthFacility.getPhone();
        if (phoneNumber != null) {
            LinearLayout llHealthFacilityCalling = (LinearLayout) v.findViewById(R.id
                    .layout_health_facility_calling);
            llHealthFacilityCalling.setVisibility(View.VISIBLE);

            Button btnCall = (Button) v.findViewById(R.id.button_calling_health_facility);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mContext.startActivity(callIntent);
                }
            });
        }

        if (healthFacility.getDistance().getText() != null) {
            TextView tvItemHealthFacilityDistance = (TextView) v.findViewById(R.id.text_item_health_facility_distance);

            if (healthFacility.getDistance().getText().equals("NO_RESULT")) {
                tvItemHealthFacilityDistance.setText("Không xác định được khoảng cách");
            } else {
                tvItemHealthFacilityDistance.setText(healthFacility.getDistance().getText() + " - " +
                        healthFacility.getDuration().getText());
            }
            tvItemHealthFacilityDistance.setVisibility(View.VISIBLE);

        }
        return v;
    }


    @Override
    public Filter getFilter() {
        if (healthFacilityFilter == null) {
            healthFacilityFilter = new HealthFacilityFilter(this);
        }
        return healthFacilityFilter;
    }
}
