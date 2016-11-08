package com.favn.firstaid.Models;

import android.util.Log;
import android.widget.Filter;

import com.favn.firstaid.Adapter.HealthFacilityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Gia on 11/8/2016.
 */

public class HealthFacilityFilter extends Filter {

    private final HealthFacilityAdapter healthFacilityAdapter;

    public HealthFacilityFilter(HealthFacilityAdapter healthFacilityAdapter) {
        this.healthFacilityAdapter = healthFacilityAdapter;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint == null || constraint.length() == 0) {
            // unfiltered: show all
            results.values = healthFacilityAdapter.getmHealthFacilityList();
            results.count = healthFacilityAdapter.getmHealthFacilityList().size();
        } else {
            // filter
            List<HealthFacility> filteredList = new ArrayList<>();

            if (constraint.equals("1")) {
                for (HealthFacility healthFacility : healthFacilityAdapter.getmHealthFacilityList()) {
                    if (healthFacility.getType() == 1) {
                        filteredList.add(healthFacility);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        healthFacilityAdapter.setmHealthFacilityList((List<HealthFacility>) results.values);
        if (results.count == 0) {
            healthFacilityAdapter.notifyDataSetInvalidated();
        } else {
            healthFacilityAdapter.notifyDataSetChanged();
        }
    }
}
