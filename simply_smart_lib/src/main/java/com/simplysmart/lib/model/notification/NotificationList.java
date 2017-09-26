package com.simplysmart.lib.model.notification;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 10/8/15.
 */
public class NotificationList implements Parcelable {

    private String category, unread_count;
    private ArrayList<Notification> data;

    protected NotificationList(Parcel in) {
        category = in.readString();
        unread_count = in.readString();
        data = in.createTypedArrayList(Notification.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(unread_count);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationList> CREATOR = new Creator<NotificationList>() {
        @Override
        public NotificationList createFromParcel(Parcel in) {
            return new NotificationList(in);
        }

        @Override
        public NotificationList[] newArray(int size) {
            return new NotificationList[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }

    public ArrayList<Notification> getData() {
        return data;
    }

    public void setData(ArrayList<Notification> data) {
        this.data = data;
    }

    public int getCategoryUnReadCount() {
        if ((unread_count != null) && !unread_count.equalsIgnoreCase("")) {
            return Integer.parseInt(unread_count);
        }

        return 0;
    }
}
