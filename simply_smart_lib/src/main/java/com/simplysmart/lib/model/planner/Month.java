package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class Month implements Parcelable {

    private Value min;
    private Value max;
    private Value avg;

    public Month() {
    }

    protected Month(Parcel in) {
        min = in.readParcelable(Value.class.getClassLoader());
        max = in.readParcelable(Value.class.getClassLoader());
        avg = in.readParcelable(Value.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(min, flags);
        dest.writeParcelable(max, flags);
        dest.writeParcelable(avg, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Month> CREATOR = new Creator<Month>() {
        @Override
        public Month createFromParcel(Parcel in) {
            return new Month(in);
        }

        @Override
        public Month[] newArray(int size) {
            return new Month[size];
        }
    };

    public Value getMin() {
        return min;
    }

    public void setMin(Value min) {
        this.min = min;
    }

    public Value getMax() {
        return max;
    }

    public void setMax(Value max) {
        this.max = max;
    }

    public Value getAvg() {
        return avg;
    }

    public void setAvg(Value avg) {
        this.avg = avg;
    }
}
