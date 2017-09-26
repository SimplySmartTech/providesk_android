package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 17/8/15.
 */
public class LoginData implements Parcelable {

    private Resident resident;

    protected LoginData(Parcel in) {
        resident = in.readParcelable(Resident.class.getClassLoader());
    }

    public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
        @Override
        public LoginData createFromParcel(Parcel in) {
            return new LoginData(in);
        }

        @Override
        public LoginData[] newArray(int size) {
            return new LoginData[size];
        }
    };

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(resident, flags);
    }
}
