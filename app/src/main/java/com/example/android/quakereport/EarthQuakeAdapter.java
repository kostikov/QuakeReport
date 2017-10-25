package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        magTextView.setText(String.valueOf(currentEarthQuake.getmMag()));

        TextView cityTextView = (TextView) listItemView.findViewById(R.id.location);
        cityTextView.setText(currentEarthQuake.getmCity());

        Date date = new Date(currentEarthQuake.getmTimeInMilliseconds());
        String formattedData = formatDate(date);
        String formattedTime = formatTime(date);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(formattedData);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(formattedTime);

        return listItemView;


    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    private String formatTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return timeFormat.format(date);
    }
}
