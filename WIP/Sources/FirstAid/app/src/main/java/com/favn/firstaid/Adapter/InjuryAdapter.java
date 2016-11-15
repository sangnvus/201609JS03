package com.favn.firstaid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.R;

import java.util.List;

import static com.favn.firstaid.Models.Commons.Constants.LISTVIEW_EMERGENCY;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class InjuryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Injury> mInjuryList;
    private int controlType; // Add for check control type - by kienmt 11/04/2016

    public InjuryAdapter(Context mContext, List<Injury> mInjuryList, int controlType) {
        this.mContext = mContext;
        this.mInjuryList = mInjuryList;
        this.controlType = controlType;
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
//        View v = View.inflate(mContext, R.layout.item_injury, null);
//        TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
//        txtView.setText(mInjuryList.get(position).getInjuryName());
//        return v;

        // Edit - Check control type to return view - by Kienmt 11/04/2016
        if (controlType == LISTVIEW_EMERGENCY){
            View v = View.inflate(mContext, R.layout.item_injury, null);
            TextView txtView = (TextView)v.findViewById(R.id.text_injury_item);
            ImageView icon = (ImageView) v.findViewById(R.id.image_injury_item);
            txtView.setText(mInjuryList.get(position).getInjuryName());

            if(position == 0) {icon.setImageResource(R.drawable.img_allergies_anaphylaxis);}
            else if(position == 1){icon.setImageResource(R.drawable.img_asthma_attack);}
            else if(position == 2){icon.setImageResource(R.drawable.img_bleeding);}
            else if(position == 3){icon.setImageResource(R.drawable.img_broken_bone);}
            else if(position == 4){icon.setImageResource(R.drawable.img_burns);}
            else if(position == 5){icon.setImageResource(R.drawable.img_chocking);}
            else if(position == 6){icon.setImageResource(R.drawable.img_concussion_head_injury);}
            else if(position == 7){icon.setImageResource(R.drawable.img_diabetic_emergency);}
            else if(position == 8){icon.setImageResource(R.drawable.img_distress);}
            else if(position == 9){icon.setImageResource(R.drawable.img_heart_attack);}
            else if(position == 10){icon.setImageResource(R.drawable.img_heat_stroke);}
            else if(position == 11){icon.setImageResource(R.drawable.img_hypothermia);}
            else if(position == 12){icon.setImageResource(R.drawable.img_meningitis);}
            else if(position == 13){icon.setImageResource(R.drawable.img_poisoning_harmful_substance);}
            else if(position == 14){icon.setImageResource(R.drawable.img_seizure_epilepsy);}
            else if(position == 15){icon.setImageResource(R.drawable.img_insect_stings_bites);}
            else if(position == 16){icon.setImageResource(R.drawable.img_strains_sprains);}
            else if(position == 17){icon.setImageResource(R.drawable.img_stroke);}
            else if(position == 19){icon.setImageResource(R.drawable.img_unresponsive_and_breathing);}
            else if(position == 20){icon.setImageResource(R.drawable.img_unresponsive_and_not_breathing);}

            return v;
        } else {
            View v = View.inflate(mContext, R.layout.spinner_layout, null);
            TextView txtInjuryName = (TextView) v.findViewById(R.id.text_injury_name);
            txtInjuryName.setText(mInjuryList.get(position).getInjuryName());
            return v;
        }
    }
}
