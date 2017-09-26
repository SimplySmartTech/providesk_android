package com.simplysmart.lib.model.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 10/8/15.
 */
public class NotificationResponse implements Parcelable {

    private NotificationData data;

    protected NotificationResponse(Parcel in) {
        data = in.readParcelable(NotificationData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationResponse> CREATOR = new Creator<NotificationResponse>() {
        @Override
        public NotificationResponse createFromParcel(Parcel in) {
            return new NotificationResponse(in);
        }

        @Override
        public NotificationResponse[] newArray(int size) {
            return new NotificationResponse[size];
        }
    };

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }


}
