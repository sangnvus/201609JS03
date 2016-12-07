package com.favn.firstaid.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.firstaid.R;

import java.util.List;

/**
 * Created by Mikey on 12/6/2016.
 */

public class ItemsListAdapter extends BaseAdapter {

    public class Item {
        Drawable ItemDrawable;
        String ItemString;
        public Item(Drawable drawable, String t){
            ItemDrawable = drawable;
            ItemString = t;
        }
    }

    public static class ViewHolder {
        ImageView icon;
        TextView text;
    }

    public ItemsListAdapter() {
    }

    private Context context;
    private List<Item> list;

    public ItemsListAdapter(Context c, List<Item> l){
        context = c;
        list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.target_item, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
            viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.icon.setImageDrawable(list.get(position).ItemDrawable);
        holder.text.setText(list.get(position).ItemString);

        return rowView;
    }
}
