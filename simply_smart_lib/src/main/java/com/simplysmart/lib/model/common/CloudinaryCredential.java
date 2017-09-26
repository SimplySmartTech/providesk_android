package com.simplysmart.lib.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 06/06/16.
 */
public class CloudinaryCredential implements Parcelable {

    private String timestamp;
    private String signature;
    private String api_key;

    protected CloudinaryCredential(Parcel in) {
        timestamp = in.readString();
        signature = in.readString();
        api_key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(signature);
        dest.writeString(api_key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CloudinaryCredential> CREATOR = new Creator<CloudinaryCredential>() {
        @Override
        public CloudinaryCredential createFromParcel(Parcel in) {
            return new CloudinaryCredential(in);
        }

        @Override
        public CloudinaryCredential[] newArray(int size) {
            return new CloudinaryCredential[size];
        }
    };

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
