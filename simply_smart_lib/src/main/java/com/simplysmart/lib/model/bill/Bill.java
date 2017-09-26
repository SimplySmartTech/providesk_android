package com.simplysmart.lib.model.bill;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shekhar on 07/06/17.
 */

public class Bill implements Parcelable {

    private String unit_info;
    private String aasm_state;
    private String name;
    private String utility_type;

    private String bill_number;
    private String bill_date;
    private String start_date;
    private String end_date;
    private String due_date_formatted;

    private String units_consumed;
    private String energy_charge;
    private String tax_amount;
    private String net_amount;
    private HashMap<String, String> other_charges;

    private ArrayList<DueCharge> due_charges;

    protected Bill(Parcel in) {
        unit_info = in.readString();
        aasm_state = in.readString();
        name = in.readString();
        utility_type = in.readString();
        bill_number = in.readString();
        bill_date = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        due_date_formatted = in.readString();
        units_consumed = in.readString();
        energy_charge = in.readString();
        tax_amount = in.readString();
        net_amount = in.readString();
        due_charges = in.createTypedArrayList(DueCharge.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(unit_info);
        dest.writeString(aasm_state);
        dest.writeString(name);
        dest.writeString(utility_type);
        dest.writeString(bill_number);
        dest.writeString(bill_date);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(due_date_formatted);
        dest.writeString(units_consumed);
        dest.writeString(energy_charge);
        dest.writeString(tax_amount);
        dest.writeString(net_amount);
        dest.writeTypedList(due_charges);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public ArrayList<DueCharge> getDue_charges() {
        return due_charges;
    }

    public void setDue_charges(ArrayList<DueCharge> due_charges) {
        this.due_charges = due_charges;
    }

    public String getUnits_consumed() {
        return units_consumed;
    }

    public void setUnits_consumed(String units_consumed) {
        this.units_consumed = units_consumed;
    }

    public String getEnergy_charge() {
        return energy_charge;
    }

    public void setEnergy_charge(String energy_charge) {
        this.energy_charge = energy_charge;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount = net_amount;
    }

    public HashMap<String, String> getOther_charges() {
        return other_charges;
    }

    public void setOther_charges(HashMap<String, String> other_charges) {
        this.other_charges = other_charges;
    }

    public String getUnit_info() {
        return unit_info;
    }

    public void setUnit_info(String unit_info) {
        this.unit_info = unit_info;
    }

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUtility_type() {
        return utility_type;
    }

    public void setUtility_type(String utility_type) {
        this.utility_type = utility_type;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDue_date_formatted() {
        return due_date_formatted;
    }

    public void setDue_date_formatted(String due_date_formatted) {
        this.due_date_formatted = due_date_formatted;
    }
}
