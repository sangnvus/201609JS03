package com.favn.firstaid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.models.LearningInstruction;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Mikey on 11/16/2016.
 */

public class LearningInstructionAdapter extends BaseAdapter {
    private Context mContext;
    private List<LearningInstruction> mInstructionList;

    public LearningInstructionAdapter(Context mContext, List<LearningInstruction> mInjuryList) {
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
        LearningInstruction learningInstruction = mInstructionList.get(position);
        View v = View.inflate(mContext, R.layout.item_instruction, null);
        TextView tvStep = (TextView) v.findViewById(R.id.text_step_number);
        TextView tvInstruction = (TextView) v.findViewById(R.id.text_instruction_content);
        TextView tvExplanation = (TextView) v.findViewById(R.id.text_instruction_explaination);
        Button call = (Button) v.findViewById(R.id.button_call);
        View line = (View) v.findViewById(R.id.line);

        ImageView imgImage = (ImageView) v.findViewById(R.id.image_instruction);

        //TODO check null for image property
        int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                learningInstruction.getImage(), null, null);

        int audio = v.getResources().getIdentifier("com.favn.firstaid:raw/" +
                learningInstruction.getAudio(), null, null);

        tvStep.setText(learningInstruction.getStep() + "");
        tvInstruction.setText(learningInstruction.getContent());
        if(learningInstruction.getExplanation() != null) {
            tvExplanation.setVisibility(View.VISIBLE);
            tvExplanation.setText(learningInstruction.getExplanation());
        }
        line.setVisibility(View.GONE);
        call.setVisibility(View.GONE);

        imgImage.setImageResource(imagePath);

//        Intent intent = new Intent(InstructionAdapter.this, InstructionDetail.class);

        return v;
    }
}
