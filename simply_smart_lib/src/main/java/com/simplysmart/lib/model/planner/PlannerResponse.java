package com.simplysmart.lib.model.planner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 02/06/16.
 */
public class PlannerResponse implements Parcelable {

    private PlannerData data;
    private String message;

    protected PlannerResponse(Parcel in) {
        data = in.readParcelable(PlannerData.class.getClassLoader());
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlannerResponse> CREATOR = new Creator<PlannerResponse>() {
        @Override
        public PlannerResponse createFromParcel(Parcel in) {
            return new PlannerResponse(in);
        }

        @Override
        public PlannerResponse[] newArray(int size) {
            return new PlannerResponse[size];
        }
    };

    public PlannerData getData() {
        return data;
    }

    public void setData(PlannerData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
