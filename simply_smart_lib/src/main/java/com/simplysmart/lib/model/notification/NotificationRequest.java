package com.simplysmart.lib.model.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 10/23/15.
 */
public class NotificationRequest implements Parcelable {
    private Data notification;

    public NotificationRequest() {
    }

    protected NotificationRequest(Parcel in) {
        notification = in.readParcelable(Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(notification, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRequest> CREATOR = new Creator<NotificationRequest>() {
        @Override
        public NotificationRequest createFromParcel(Parcel in) {
            return new NotificationRequest(in);
        }

        @Override
        public NotificationRequest[] newArray(int size) {
            return new NotificationRequest[size];
        }
    };

    public Data getNotification() {
        return notification;
    }

    public void setNotification(Data notification) {
        this.notification = notification;
    }

    public static class Data implements Parcelable {

        private String read_at;
        private String message;

        public Data() {

        }

        protected Data(Parcel in) {
            read_at = in.readString();
            message = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(read_at);
            dest.writeString(message);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public String getRead_at() {
            return read_at;
        }

        public void setRead_at(String read_at) {
            this.read_at = read_at;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}