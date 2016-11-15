package com.favn.firstaid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.favn.firstaid.Models.Faq;

import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Mikey on 11/12/2016.
 */

public class FaqAdapter extends BaseAdapter {

    private Context mContext;
    private List<Faq> mFaqList;

    public FaqAdapter(Context mContext, List<Faq> mFaqList) {
        this.mContext = mContext;
        this.mFaqList = mFaqList;
    }

    @Override
    public int getCount() {
        return mFaqList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFaqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFaqList.get(position).getInjuryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Faq faq = mFaqList.get(position);
        View v = View.inflate(mContext, R.layout.item_faq, null);
        TextView tvQuestion = (TextView) v.findViewById(R.id.text_question);
        TextView tvAnswer = (TextView) v.findViewById(R.id.text_answer);

        tvQuestion.setText(faq.getQuestion());
        tvAnswer.setText(faq.getAnswer());
        return v;
    }
}
