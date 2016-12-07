package com.favn.firstaid.fragments;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.favn.firstaid.R;
import com.favn.firstaid.activites.BannerDetail;
import com.favn.firstaid.activites.QAActivity;
import com.favn.firstaid.adapters.ItemsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    private ItemsListAdapter myItemsListAdapter;
    private List<ItemsListAdapter.Item> items;
    private int InjId = 0;
    public MoreFragment() {
        // Required empty public constructor
    }

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
                    intent.putExtra("id", InjId);
                    startActivity(intent);
                }
                else if (position == 1) {
                    Intent intent = new Intent(getActivity(), BannerDetail.class);
                    startActivity(intent);
                }
                else if (position == 2) {
                    shareIt();
                }
            }
        });

        container.removeAllViews();
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return rootView;
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Demo sharing");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hot boy keo con voi https://www.facebook.com/your.vitieubu/ ");
        startActivity(Intent.createChooser(sharingIntent, "Chia sẻ ứng dụng"));
    }

    private void initItems(){

        items = new ArrayList<ItemsListAdapter.Item>();

        TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.more_icon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.more_item);

        for(int i=0; i<arrayDrawable.length(); i++){
            Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            ItemsListAdapter.Item item = new ItemsListAdapter().new Item(d, s);
            items.add(item);
        }

        arrayDrawable.recycle();
        arrayText.recycle();
    }

}
