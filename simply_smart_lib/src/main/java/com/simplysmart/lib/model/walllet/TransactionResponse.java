package com.simplysmart.lib.model.walllet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 17/01/17.
 */

public class TransactionResponse implements Parcelable {

    TransactionData data;

    protected TransactionResponse(Parcel in) {
        data = in.readParcelable(TransactionData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionResponse> CREATOR = new Creator<TransactionResponse>() {
        @Override
        public TransactionResponse createFromParcel(Parcel in) {
            return new TransactionResponse(in);
        }

        @Override
        public TransactionResponse[] newArray(int size) {
            return new TransactionResponse[size];
        }
    };

    public TransactionData getData() {
        return data;
    }

    public void setData(TransactionData data) {
        this.data = data;
    }
}
