package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class Planner implements Parcelable {

    private boolean amc;
    private int duration;
    private int electricity;
    private String unit_id;
    private int water;
    private double ewallet_balance;
    private Ranges ranges;

    public Planner() {

    }

    protected Planner(Parcel in) {
        amc = in.readByte() != 0;
        duration = in.readInt();
        electricity = in.readInt();
        unit_id = in.readString();
        water = in.readInt();
        ewallet_balance = in.readDouble();
        ranges = in.readParcelable(Ranges.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (amc ? 1 : 0));
        dest.writeInt(duration);
        dest.writeInt(electricity);
        dest.writeString(unit_id);
        dest.writeInt(water);
        dest.writeDouble(ewallet_balance);
        dest.writeParcelable(ranges, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Planner> CREATOR = new Creator<Planner>() {
        @Override
        public Planner createFromParcel(Parcel in) {
            return new Planner(in);
        }

        @Override
        public Planner[] newArray(int size) {
            return new Planner[size];
        }
    };

    public boolean isAmc() {
        return amc;
    }

    public void setAmc(boolean amc) {
        this.amc = amc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public double getEwallet_balance() {
        return ewallet_balance;
    }

    public void setEwallet_balance(double ewallet_balance) {
        this.ewallet_balance = ewallet_balance;
    }

    public Ranges getRanges() {
        return ranges;
    }

    public void setRanges(Ranges ranges) {
        this.ranges = ranges;
    }
}
