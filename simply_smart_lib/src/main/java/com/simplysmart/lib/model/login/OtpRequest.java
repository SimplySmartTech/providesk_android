package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 26/09/17.
 */

public class OtpRequest implements Parcelable {

    private String otp_code;

    public OtpRequest(Parcel in) {
        otp_code = in.readString();
    }

    public static final Creator<OtpRequest> CREATOR = new Creator<OtpRequest>() {
        @Override
        public OtpRequest createFromParcel(Parcel in) {
            return new OtpRequest(in);
        }

        @Override
        public OtpRequest[] newArray(int size) {
            return new OtpRequest[size];
        }
    };

    public OtpRequest(String otp) {
        this.otp_code = otp;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
