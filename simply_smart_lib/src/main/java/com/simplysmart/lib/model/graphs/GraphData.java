package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 28/10/15.
 */
public class GraphData implements Parcelable {

    private ArrayList<MeterUnitValue> units;
    private ArrayList<MeterBill> bill;


    protected GraphData(Parcel in) {
        units = in.createTypedArrayList(MeterUnitValue.CREATOR);
        bill = in.createTypedArrayList(MeterBill.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(units);
        dest.writeTypedList(bill);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GraphData> CREATOR = new Creator<GraphData>() {
        @Override
        public GraphData createFromParcel(Parcel in) {
            return new GraphData(in);
        }

        @Override
        public GraphData[] newArray(int size) {
            return new GraphData[size];
        }
    };

    public ArrayList<MeterBill> getBill() {
        return bill;
    }

    public void setBill(ArrayList<MeterBill> bill) {
        this.bill = bill;
    }

    public ArrayList<MeterUnitValue> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<MeterUnitValue> units) {
        this.units = units;
    }
}
