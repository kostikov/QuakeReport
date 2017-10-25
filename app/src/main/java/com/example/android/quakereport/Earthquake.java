package com.example.android.quakereport;

import java.util.Date;

public class Earthquake {

    private double mMag;
    private String mCity;
    private Date mDate;

    public Earthquake(double mMag, String mCity, long mDate) {
        this.mMag = mMag;
        this.mCity = mCity;
        this.mDate = new Date(mDate);
    }

    public double getmMag() {
        return mMag;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmDate() {
        return mDate.toString();
    }

}
