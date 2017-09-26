package com.simplysmart.lib.model.walllet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 03/02/17.
 */
public class WalletCredentialRequest implements Parcelable {

    private String resident_id;
    private String gateway;
    private String utility_type;
    private String unit_id;
    private String amount;
    private boolean sandbox;

    private String status;
    private String payment_success_id;

    protected WalletCredentialRequest(Parcel in) {
        resident_id = in.readString();
        gateway = in.readString();
        utility_type = in.readString();
        unit_id = in.readString();
        amount = in.readString();
        sandbox = in.readByte() != 0;
        status = in.readString();
        payment_success_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resident_id);
        dest.writeString(gateway);
        dest.writeString(utility_type);
        dest.writeString(unit_id);
        dest.writeString(amount);
        dest.writeByte((byte) (sandbox ? 1 : 0));
        dest.writeString(status);
        dest.writeString(payment_success_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletCredentialRequest> CREATOR = new Creator<WalletCredentialRequest>() {
        @Override
        public WalletCredentialRequest createFromParcel(Parcel in) {
            return new WalletCredentialRequest(in);
        }

        @Override
        public WalletCredentialRequest[] newArray(int size) {
            return new WalletCredentialRequest[size];
        }
    };

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_success_id() {
        return payment_success_id;
    }

    public void setPayment_success_id(String payment_success_id) {
        this.payment_success_id = payment_success_id;
    }

    public WalletCredentialRequest() {

    }

    public String getUtility_type() {
        return utility_type;
    }

    public void setUtility_type(String utility_type) {
        this.utility_type = utility_type;
    }

    public String getResident_id() {
        return resident_id;
    }

    public void setResident_id(String resident_id) {
        this.resident_id = resident_id;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

