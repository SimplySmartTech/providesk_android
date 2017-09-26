package com.simplysmart.lib.model.bill;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 07/06/17.
 */

public class BillData implements Parcelable{

    private Bill bill;

    protected BillData(Parcel in) {
        bill = in.readParcelable(Bill.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bill, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BillData> CREATOR = new Creator<BillData>() {
        @Override
        public BillData createFromParcel(Parcel in) {
            return new BillData(in);
        }

        @Override
        public BillData[] newArray(int size) {
            return new BillData[size];
        }
    };

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
