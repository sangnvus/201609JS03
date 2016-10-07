package com.favn.firstaid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class InstructionAdapter extends BaseAdapter{
    private Context mContext;
    private List<Instruction> mInstructionList;

    public InstructionAdapter(Context mContext, List<Instruction> mInjuryList) {
        this.mContext = mContext;
        this.mInstructionList = mInjuryList;
    }

    @Override
    public int getCount() {
        return mInstructionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInstructionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mInstructionList.get(position).getInjuryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_instruction, null);
        TextView txtView = (TextView)v.findViewById(R.id.text_instruction_content);
        txtView.setText(mInstructionList.get(position).getStep());
        return v;
    }
}
