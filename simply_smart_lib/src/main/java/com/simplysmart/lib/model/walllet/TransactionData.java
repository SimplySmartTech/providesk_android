package com.simplysmart.lib.model.walllet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 17/01/17.
 */
public class TransactionData implements Parcelable {

    ArrayList<Transaction> transactions;
    private String balance;

    protected TransactionData(Parcel in) {
        transactions = in.createTypedArrayList(Transaction.CREATOR);
        balance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(transactions);
        dest.writeString(balance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionData> CREATOR = new Creator<TransactionData>() {
        @Override
        public TransactionData createFromParcel(Parcel in) {
            return new TransactionData(in);
        }

        @Override
        public TransactionData[] newArray(int size) {
            return new TransactionData[size];
        }
    };

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
