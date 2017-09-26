package com.simplysmart.lib.model.bill;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 07/06/17.
 */

public class BillDetailResponse implements Parcelable {

    private String message;
    private BillData data;

    protected BillDetailResponse(Parcel in) {
        message = in.readString();
        data = in.readParcelable(BillData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BillDetailResponse> CREATOR = new Creator<BillDetailResponse>() {
        @Override
        public BillDetailResponse createFromParcel(Parcel in) {
            return new BillDetailResponse(in);
        }

        @Override
        public BillDetailResponse[] newArray(int size) {
            return new BillDetailResponse[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BillData getData() {
        return data;
    }

    public void setData(BillData data) {
        this.data = data;
    }
}
