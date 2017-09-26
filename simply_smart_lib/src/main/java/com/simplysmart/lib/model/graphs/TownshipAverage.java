package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 28/10/15.
 */
public class TownshipAverage implements Parcelable {

    private String units;
    private String bill;

    protected TownshipAverage(Parcel in) {
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

    public static final Creator<TownshipAverage> CREATOR = new Creator<TownshipAverage>() {
        @Override
        public TownshipAverage createFromParcel(Parcel in) {
            return new TownshipAverage(in);
        }

        @Override
        public TownshipAverage[] newArray(int size) {
            return new TownshipAverage[size];
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
