package com.easygo.vilius.pasiklydauapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapteris vietoves duomenims isvesti
 */
public class LocationAdapter extends BaseAdapter {

    ArrayList<LocationEntry> dataList;      //Duomenu sarasas
    Activity activity;

    /**
     * Konstruktorius su parametrais
     * @param d - duomenu sarasas
     * @param a - activitis
     */
    LocationAdapter(ArrayList<LocationEntry> d, Activity a)
    {
        dataList = d;
        activity = a;
    }

    /**
     * Grazina duomeni kieki
     * @return size - dydis
     */
    public int getCount()
    {
        if (dataList != null)
        {
            return dataList.size();
        }
        return 0;
    }

    /**
     * Grazina elemento id pagal position
     * @param position - elemento pozicija
     * @return position - pozicija
     */
    public long getItemId(int position) { return position; }

    /**
     * Grazina objekta pagal pozicija
     * @param position - grazinamo objekto pozicija
     * @return objektas pagal pozicija
     */
    public Object getItem(int position)
    {
        if(dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    /**
     * Grazina View objekta
     * @param position - pozicija
     * @param convertView - converView objektas
     * @param viewGroup - viewGroup objektas
     * @return vi - View objektas
     */
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        View vi = convertView;
        if(vi == null)
        {
            LayoutInflater li = LayoutInflater.from(activity);
            vi = li.inflate(R.layout.leaderboard_list_layout, null);
        }

        TextView nameText = (TextView)vi.findViewById(R.id.listview_name);
        TextView scoreText = (TextView)vi.findViewById(R.id.listview_score);
        LocationEntry le = dataList.get(position);

        nameText.setText(le.getAddress());
        scoreText.setText(le.getDate());



        return vi;
    }
}

