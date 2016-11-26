package com.jorgedguezm.elections;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jorge on 13/11/16.
 */

public class Institution implements Parcelable {
    private String chamberName;
    private String electName;
    private int totalElects;
    private String place;
    private int year;

    public Institution(String c, String e, int t, String p, int y) {
        chamberName = c;
        electName = e;
        totalElects = t;
        place = p;
        year = y;
    }

    protected Institution(Parcel in) {
        chamberName = in.readString();
        electName = in.readString();
        totalElects = in.readInt();
        place = in.readString();
        year = in.readInt();
    }

    public static final Creator<Institution> CREATOR = new Creator<Institution>() {
        @Override
        public Institution createFromParcel(Parcel in) {
            return new Institution(in);
        }

        @Override
        public Institution[] newArray(int size) {
            return new Institution[size];
        }
    };

    public String getChamberName() {
        return chamberName;
    }

    public String getElectName() {
        return electName;
    }

    public int getTotalElects() {
        return totalElects;
    }

    public String getPlace() {
        return place;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chamberName);
        dest.writeString(electName);
        dest.writeInt(totalElects);
        dest.writeString(place);
        dest.writeInt(year);
    }
}
