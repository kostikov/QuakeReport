package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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
        GradientDrawable magCircle = (GradientDrawable) magTextView.getBackground();

        String mag = formatMagnitude(currentEarthQuake.getmMag());

        int magnitColor = magColor(currentEarthQuake.getmMag());
        magCircle.setColor(magnitColor);

        magTextView.setText(mag);

        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance);
        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);

        String distance, location;
        final String LOCATION_SEPARATOR = " of ";

        String place = currentEarthQuake.getmCity();
        if (place.contains(LOCATION_SEPARATOR)) {
            String parts[] = place.split(LOCATION_SEPARATOR);
            distance = parts[0] + LOCATION_SEPARATOR;
            location = parts[1];
        } else {
            distance = getContext().getString(R.string.near);
            location = place;
        }

        distanceTextView.setText(distance.toUpperCase());
        locationTextView.setText(location);

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

    private String formatMagnitude(double mag) {
        DecimalFormat formatingMag = new DecimalFormat("0.0");
        return formatingMag.format(mag);
    }

    private int magColor(double mag) {

        int magInt = (int) Math.floor(mag);
        int color;
        switch (magInt) {

            case 0:
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(getContext(), color);
    }
}
