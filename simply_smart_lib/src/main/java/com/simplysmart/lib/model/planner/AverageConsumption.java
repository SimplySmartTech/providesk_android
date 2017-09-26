package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class AverageConsumption implements Parcelable {

    private double electricity;
    private double water;

    protected AverageConsumption(Parcel in) {
        electricity = in.readDouble();
        water = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(electricity);
        dest.writeDouble(water);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AverageConsumption> CREATOR = new Creator<AverageConsumption>() {
        @Override
        public AverageConsumption createFromParcel(Parcel in) {
            return new AverageConsumption(in);
        }

        @Override
        public AverageConsumption[] newArray(int size) {
            return new AverageConsumption[size];
        }
    };

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }
}
