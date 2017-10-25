package com.example.android.quakereport;

public class Earthquake {

    private String mMag;
    private String mCity;
    private long mTimeInMilliseconds;

    public Earthquake(String mMag, String mCity, long mTimeInMilliseconds) {
        this.mMag = mMag;
        this.mCity = mCity;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
    }

    public String getmMag() {
        return mMag;
    }

    public String getmCity() {
        return mCity;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

}
