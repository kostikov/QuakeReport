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

    public void setmMag(float mMag) {
        this.mMag = mMag;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmDate() {
        return mDate.toString();
    }

    public void setmDate(long mDate) {
        this.mDate = new Date(mDate);
    }
}
