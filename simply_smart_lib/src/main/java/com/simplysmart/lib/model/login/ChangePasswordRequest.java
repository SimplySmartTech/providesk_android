package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 09/02/17.
 */
public class ChangePasswordRequest implements Parcelable {

    private Resident resident;

    public ChangePasswordRequest(Parcel in) {
        resident = in.readParcelable(Resident.class.getClassLoader());
    }

    public ChangePasswordRequest() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(resident, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChangePasswordRequest> CREATOR = new Creator<ChangePasswordRequest>() {
        @Override
        public ChangePasswordRequest createFromParcel(Parcel in) {
            return new ChangePasswordRequest(in);
        }

        @Override
        public ChangePasswordRequest[] newArray(int size) {
            return new ChangePasswordRequest[size];
        }
    };

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }
}
