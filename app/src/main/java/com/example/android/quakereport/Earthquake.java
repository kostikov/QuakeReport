package com.example.android.quakereport;

public class Earthquake {

    private double mMag;
    private String mCity;
    private long mTimeInMilliseconds;

    private String mUrl;

    public Earthquake(double mMag, String mCity, long mTimeInMilliseconds, String mUrl) {
        this.mMag = mMag;
        this.mCity = mCity;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.mUrl = mUrl;
    }

    public double getmMag() {
        return mMag;
    }

    public String getmCity() {
        return mCity;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getmUrl() {
        return mUrl;
    }

}
