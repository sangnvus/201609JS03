package com.favn.firstaid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.favn.firstaid.R;
import com.favn.firstaid.models.Instruction;

import java.util.List;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class InstructionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Instruction> mInstructionList;
    private boolean isEmergency;
    private InformationSending informationSending;

    public interface InformationSending {
        void requestInformationSending();
    }

    public InstructionAdapter(Context Context, InformationSending informationSending, List<Instruction>
            mInjuryList, boolean isEmergency) {
        this.mContext = Context;
        this.mInstructionList = mInjuryList;
        this.isEmergency = isEmergency;
        this.informationSending = informationSending;
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
        TextView tvExplanation = (TextView) v.findViewById(R.id.text_instruction_explaination);
        tvExplanation.setVisibility(View.GONE);

        ImageView imgImage = (ImageView) v.findViewById(R.id.image_instruction);

        //TODO check null for image property
        int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                instruction.getImage(), null, null);

        int audio = v.getResources().getIdentifier("com.favn.firstaid:raw/" +
                instruction.getAudio(), null, null);

        tvStep.setText(instruction.getStep() + "");
        tvInstruction.setText(instruction.getContent());

        if (instruction.isMakeCall() == true) {
            LinearLayout callLayout = (LinearLayout) v.findViewById(R.id.instruction_call);
            callLayout.setVisibility(View.VISIBLE);

            Button call115 = (Button) v.findViewById(R.id.button_call);
            call115.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informationSending.requestInformationSending();
                }
            });
        }

        imgImage.setImageResource(imagePath);


        return v;
    }


}
