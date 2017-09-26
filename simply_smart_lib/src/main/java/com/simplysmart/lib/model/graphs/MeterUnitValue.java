package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 11/05/17.
 */

public class MeterUnitValue implements Parcelable {

    private String date;
    private UnitValue value;

    protected MeterUnitValue(Parcel in) {
        date = in.readString();
        value = in.readParcelable(UnitValue.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeParcelable(value, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeterUnitValue> CREATOR = new Creator<MeterUnitValue>() {
        @Override
        public MeterUnitValue createFromParcel(Parcel in) {
            return new MeterUnitValue(in);
        }

        @Override
        public MeterUnitValue[] newArray(int size) {
            return new MeterUnitValue[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UnitValue getValue() {
        return value;
    }

    public void setValue(UnitValue value) {
        this.value = value;
    }
}
