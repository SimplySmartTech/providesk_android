package com.simplysmart.lib.model.bots;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 27/10/15.
 */
public class BotResponse implements Parcelable {

    private BotData data;

    protected BotResponse(Parcel in) {
        data = in.readParcelable(BotData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BotResponse> CREATOR = new Creator<BotResponse>() {
        @Override
        public BotResponse createFromParcel(Parcel in) {
            return new BotResponse(in);
        }

        @Override
        public BotResponse[] newArray(int size) {
            return new BotResponse[size];
        }
    };

    public BotData getData() {
        return data;
    }

    public void setData(BotData data) {
        this.data = data;
    }


}
