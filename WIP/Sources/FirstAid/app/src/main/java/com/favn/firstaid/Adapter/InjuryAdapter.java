package com.favn.firstaid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class InjuryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Injury> mInjuryList;

    public InjuryAdapter(Context mContext, List<Injury> mInjuryList) {
        this.mContext = mContext;
        this.mInjuryList = mInjuryList;
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
        View v = View.inflate(mContext, R.layout.item_injury, null);
        TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
        txtView.setText(mInjuryList.get(position).getInjuryName());
        return v;
    }
}
