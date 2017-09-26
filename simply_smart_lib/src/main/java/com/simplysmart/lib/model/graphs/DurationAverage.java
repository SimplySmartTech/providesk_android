package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 28/10/15.
 */
public class DurationAverage implements Parcelable {

    private String units;
    private String bill;

    protected DurationAverage(Parcel in) {
        units = in.readString();
        bill = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(units);
        dest.writeString(bill);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DurationAverage> CREATOR = new Creator<DurationAverage>() {
        @Override
        public DurationAverage createFromParcel(Parcel in) {
            return new DurationAverage(in);
        }

        @Override
        public DurationAverage[] newArray(int size) {
            return new DurationAverage[size];
        }
    };

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }


}
