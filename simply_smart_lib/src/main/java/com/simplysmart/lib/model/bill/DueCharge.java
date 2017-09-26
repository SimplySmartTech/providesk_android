package com.simplysmart.lib.model.bill;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 07/06/17.
 */

public class DueCharge implements Parcelable{

    private String due_amount;
    private String name;

    public DueCharge(Parcel in) {
        due_amount = in.readString();
        name = in.readString();
    }

    public DueCharge() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(due_amount);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DueCharge> CREATOR = new Creator<DueCharge>() {
        @Override
        public DueCharge createFromParcel(Parcel in) {
            return new DueCharge(in);
        }

        @Override
        public DueCharge[] newArray(int size) {
            return new DueCharge[size];
        }
    };

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
