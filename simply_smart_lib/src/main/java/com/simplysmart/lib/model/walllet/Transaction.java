package com.simplysmart.lib.model.walllet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 17/01/17.
 */
public class Transaction implements Parcelable {

    private String comment;
    private String net_amount;
    private String transaction_number;
    private String transaction_type = "";
    private String transaction_at;
    private String note;
    private String id;
    private String aasm_state;

    protected Transaction(Parcel in) {
        comment = in.readString();
        net_amount = in.readString();
        transaction_number = in.readString();
        transaction_type = in.readString();
        transaction_at = in.readString();
        note = in.readString();
        id = in.readString();
        aasm_state = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(net_amount);
        dest.writeString(transaction_number);
        dest.writeString(transaction_type);
        dest.writeString(transaction_at);
        dest.writeString(note);
        dest.writeString(id);
        dest.writeString(aasm_state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    public String getTransaction_at() {
        return transaction_at;
    }

    public void setTransaction_at(String transaction_at) {
        this.transaction_at = transaction_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount = net_amount;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    public void setTransaction_number(String transaction_number) {
        this.transaction_number = transaction_number;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
