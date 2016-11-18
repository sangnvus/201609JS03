package com.favn.firstaid.Adapter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.favn.firstaid.Activites.InstructionDetail;
import com.favn.firstaid.Models.Instruction;
import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class InstructionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Instruction> mInstructionList;
    private boolean isEmergency;
    private boolean isSendInformation;

    public interface Implementable{
        void passData(int position);
    }

    Implementable implementable;

    public InstructionAdapter(Context Context, List<Instruction> mInjuryList, boolean
            isEmergency, boolean isSendInformation) {
        this.mContext = Context;
        this.mInstructionList = mInjuryList;
        this.isEmergency = isEmergency;
        this.isSendInformation = isSendInformation;

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

        ImageView imgImage = (ImageView) v.findViewById(R.id.image_instruction);

        //TODO check null for image property
        int imagePath = v.getResources().getIdentifier("com.favn.firstaid:drawable/" +
                instruction.getImage(), null, null);

        int audio = v.getResources().getIdentifier("com.favn.firstaid:raw/" +
                instruction.getAudio(), null, null);

        tvStep.setText(instruction.getStep() + "");
        tvInstruction.setText(instruction.getContent());
        if (!isEmergency) {
            TextView tvExplanation = (TextView) v.findViewById(R.id.text_instruction_explaination);
            tvExplanation.setText(instruction.getExplanation());
        }

        if (instruction.isMakeCall() == true) {
            LinearLayout callLayout = (LinearLayout) v.findViewById(R.id.instruction_call);
            callLayout.setVisibility(View.VISIBLE);
            implementable = new InstructionDetail();
            if(position >= 0) {
            implementable.passData(position);
            }

            Button call115 = (Button) v.findViewById(R.id.button_call);
            call115.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:115"));
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
                    // Get location
//                    if (isSendInformation) {
//                        InstructionDetail instructionDetail = new InstructionDetail();
//                        LocationFinder locationFinder = new LocationFinder(mContext, instructionDetail);
//                        locationFinder.buildLocationFinder();
//                        locationFinder.connectGoogleApiClient();
//                    }

                }
            });
        }

        imgImage.setImageResource(imagePath);


        return v;
    }

}
