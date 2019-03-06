package com.example.admin.myapplication.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> timeList = new ArrayList<>();
    private LayoutInflater inflter;

    public NavigationMenuAdapter(Context c, List<String> timeList) {
        mContext = c;
        this.timeList = timeList;
        inflter = (LayoutInflater.from(mContext));
    }

    public int getCount() {
        return timeList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflter.inflate(R.layout.menu_adapter_inflate, parent, false);
        }
        TextView names = v.findViewById(R.id.menuTv);
        names.setText(timeList.get(position));
        return v;
    }
}