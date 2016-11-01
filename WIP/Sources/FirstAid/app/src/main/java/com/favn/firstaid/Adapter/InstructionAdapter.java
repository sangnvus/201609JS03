package com.favn.firstaid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.Activites.InstructionDetail;
import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class InstructionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Instruction> mInstructionList;
    private boolean isEmegency;

    public InstructionAdapter(Context mContext, List<Instruction> mInjuryList, boolean isEmegency) {
        this.mContext = mContext;
        this.mInstructionList = mInjuryList;
        this.isEmegency = isEmegency;
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
        TextView tvStep = (TextView) v.findViewById(R.id.text_step_number);
        TextView tvInstruction = (TextView) v.findViewById(R.id.text_instruction_content);

        ImageView imgImage = (ImageView) v.findViewById(R.id.image_instruction);

        //TODO check null for image property
        int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                mInstructionList.get(position).getImage(), null, null);

//        InputStream is = getClass().getResourceAsStream("com.favn.firstaid:drawable/" +
//                mInstructionList.get(position).getAudio());

        tvStep.setText(mInstructionList.get(position).getStep() + "");
        tvInstruction.setText(mInstructionList.get(position).getInstruction());
        if(!isEmegency){
            TextView tvExplanation = (TextView) v.findViewById(R.id.text_instruction_explaination);
            tvExplanation.setText("chưa có nội dung");
        }

        imgImage.setImageResource(imagePath);

//        Intent intent = new Intent(InstructionAdapter.this, InstructionDetail.class);

        return v;
    }
}
