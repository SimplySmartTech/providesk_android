package com.simplysmart.lib.model.graphs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 11/05/17.
 */

public class UnitValue implements Parcelable {

    private String regular;
    private String backup;

    protected UnitValue(Parcel in) {
        regular = in.readString();
        backup = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(regular);
        dest.writeString(backup);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UnitValue> CREATOR = new Creator<UnitValue>() {
        @Override
        public UnitValue createFromParcel(Parcel in) {
            return new UnitValue(in);
        }

        @Override
        public UnitValue[] newArray(int size) {
            return new UnitValue[size];
        }
    };

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }
}
