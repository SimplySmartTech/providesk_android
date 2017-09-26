package com.simplysmart.lib.model.bots;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 14/11/16.
 */
public class LastPayment implements Parcelable {

    private String generated_at;
    private String amount;
    private String units_consumed;
    private String id;

    protected LastPayment(Parcel in) {
        generated_at = in.readString();
        amount = in.readString();
        units_consumed = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(generated_at);
        dest.writeString(amount);
        dest.writeString(units_consumed);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LastPayment> CREATOR = new Creator<LastPayment>() {
        @Override
        public LastPayment createFromParcel(Parcel in) {
            return new LastPayment(in);
        }

        @Override
        public LastPayment[] newArray(int size) {
            return new LastPayment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnits_consumed() {
        return units_consumed;
    }

    public void setUnits_consumed(String units_consumed) {
        this.units_consumed = units_consumed;
    }

    public String getGenerated_at() {
        return generated_at;
    }

    public void setGenerated_at(String generated_at) {
        this.generated_at = generated_at;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
