package com.favn.firstaid.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

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
        Instruction instruction = mInstructionList.get(position);
        View v = View.inflate(mContext, R.layout.item_instruction, null);
        TextView tvStep = (TextView) v.findViewById(R.id.text_step_number);
        TextView tvInstruction = (TextView) v.findViewById(R.id.text_instruction_content);
        Button call = (Button) v.findViewById(R.id.button_call);
        View line = (View) v.findViewById(R.id.line);

        ImageView imgImage = (ImageView) v.findViewById(R.id.image_instruction);

        //TODO check null for image property
        int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                instruction.getImage(), null, null);

        int audio = v.getResources().getIdentifier("com.favn.firstaid:raw/" +
                instruction.getAudio(), null, null);

        tvStep.setText(instruction.getStep() + "");
        tvInstruction.setText(instruction.getContent());
        if(!isEmegency){
            TextView tvExplanation = (TextView) v.findViewById(R.id.text_instruction_explaination);
            tvExplanation.setText(instruction.getExplanation());
        }

        if(instruction.isMakeCall() == true){
            line.setVisibility(View.VISIBLE);
            call.setVisibility(View.VISIBLE);
            Button call115 = (Button) v.findViewById(R.id.button_call);
            call115.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:115"));
                }
            });
        } else {
            line.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
        }

        imgImage.setImageResource(imagePath);

//        Intent intent = new Intent(InstructionAdapter.this, InstructionDetail.class);

        return v;
    }
}
