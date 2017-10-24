package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthQuakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthQuakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list,
                    parent, false);
        }

        Earthquake currentEarthQuake = getItem(position);

        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_view);
        magTextView.setText(String.valueOf(currentEarthQuake.getmMag()));

        TextView cityTextView = (TextView) listItemView.findViewById(R.id.city_view);
        cityTextView.setText(currentEarthQuake.getmCity());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_view);
        timeTextView.setText(String.valueOf(currentEarthQuake.getmDate()));

        return listItemView;


    }
}
