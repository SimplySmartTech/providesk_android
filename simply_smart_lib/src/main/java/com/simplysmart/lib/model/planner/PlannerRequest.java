package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class PlannerRequest implements Parcelable {

    Planner planner;

    public PlannerRequest() {

    }

    protected PlannerRequest(Parcel in) {
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

    public static final Creator<PlannerRequest> CREATOR = new Creator<PlannerRequest>() {
        @Override
        public PlannerRequest createFromParcel(Parcel in) {
            return new PlannerRequest(in);
        }

        @Override
        public PlannerRequest[] newArray(int size) {
            return new PlannerRequest[size];
        }
    };

    public Planner getPlanner() {
        return planner;
    }

    public void setPlanner(Planner planner) {
        this.planner = planner;
    }
}
