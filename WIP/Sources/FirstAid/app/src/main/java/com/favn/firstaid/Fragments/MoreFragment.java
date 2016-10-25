package com.favn.firstaid.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.favn.firstaid.Activites.NotificationActivity;
import com.favn.firstaid.Activites.QAActivity;
import com.favn.firstaid.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {


    public MoreFragment() {
        // Required empty public constructor
    }

    public class Item {
        Drawable ItemDrawable;
        String ItemString;
        Item(Drawable drawable, String t){
            ItemDrawable = drawable;
            ItemString = t;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l){
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

    List<Item> items;
    ItemsListAdapter myItemsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_more, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_more);

        initItems();
        myItemsListAdapter = new ItemsListAdapter(getActivity(), items);
        listView.setAdapter(myItemsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), QAActivity.class);
                    startActivity(intent);
                }
                else if (position == 1) {
                    Intent intent = new Intent(getActivity(), NotificationActivity.class);
                    startActivity(intent);
                }
                else if (position == 2) {

                }
            }
        });

        container.removeAllViews();
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return rootView;
    }

    private void initItems(){
        items = new ArrayList<Item>();

        TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.more_icon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.more_item);

        for(int i=0; i<arrayDrawable.length(); i++){
            Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            Item item = new Item(d, s);
            items.add(item);
        }

        arrayDrawable.recycle();
        arrayText.recycle();
    }

}
