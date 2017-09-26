package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class PlannerData implements Parcelable{

    private Planner planner;

    protected PlannerData(Parcel in) {
        planner = in.readParcelable(Planner.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(planner, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlannerData> CREATOR = new Creator<PlannerData>() {
        @Override
        public PlannerData createFromParcel(Parcel in) {
            return new PlannerData(in);
        }

        @Override
        public PlannerData[] newArray(int size) {
            return new PlannerData[size];
        }
    };

    public Planner getPlanner() {
        return planner;
    }

    public void setPlanner(Planner planner) {
        this.planner = planner;
    }
}
