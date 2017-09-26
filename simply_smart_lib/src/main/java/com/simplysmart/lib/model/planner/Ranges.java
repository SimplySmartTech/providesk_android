package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 05/06/16.
 */
public class Ranges implements Parcelable {

    private Month one;
    private Month three;
    private Month six;
    private Month twelve;

    protected Ranges(Parcel in) {
        one = in.readParcelable(Month.class.getClassLoader());
        three = in.readParcelable(Month.class.getClassLoader());
        six = in.readParcelable(Month.class.getClassLoader());
        twelve = in.readParcelable(Month.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(one, flags);
        dest.writeParcelable(three, flags);
        dest.writeParcelable(six, flags);
        dest.writeParcelable(twelve, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ranges> CREATOR = new Creator<Ranges>() {
        @Override
        public Ranges createFromParcel(Parcel in) {
            return new Ranges(in);
        }

        @Override
        public Ranges[] newArray(int size) {
            return new Ranges[size];
        }
    };

    public Month getOne() {
        return one;
    }

    public void setOne(Month one) {
        this.one = one;
    }

    public Month getThree() {
        return three;
    }

    public void setThree(Month three) {
        this.three = three;
    }

    public Month getSix() {
        return six;
    }

    public void setSix(Month six) {
        this.six = six;
    }

    public Month getTwelve() {
        return twelve;
    }

    public void setTwelve(Month twelve) {
        this.twelve = twelve;
    }
}
