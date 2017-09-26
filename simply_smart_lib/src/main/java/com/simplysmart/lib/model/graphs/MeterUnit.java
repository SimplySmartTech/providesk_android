package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 28/10/15.
 */
public class MeterUnit implements Parcelable {

    private String date;
    private String value;

    protected MeterUnit(Parcel in) {
        date = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeterUnit> CREATOR = new Creator<MeterUnit>() {
        @Override
        public MeterUnit createFromParcel(Parcel in) {
            return new MeterUnit(in);
        }

        @Override
        public MeterUnit[] newArray(int size) {
            return new MeterUnit[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
