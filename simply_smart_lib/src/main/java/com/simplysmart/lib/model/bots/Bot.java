package com.simplysmart.lib.model.bots;

import android.os.Parcel;
import android.os.Parcelable;

import com.simplysmart.lib.model.graphs.DurationAverage;
import com.simplysmart.lib.model.graphs.GraphData;
import com.simplysmart.lib.model.graphs.TownshipAverage;

/**
 * Created by shekhar on 27/10/15.
 */
public class Bot implements Parcelable {

    //Used to identify billing type
    private String mode;

    //Used for prepaid billing
    private String balance;
    private String total_available_days;
    private String total_available_units;
    private String units_remaining;
    private String balance_remaining;
    private boolean info_found;
    private String daily_average;

    //Used for postpaid billing
    private String unbilled_amount;
    private String units_consumed;
    private LastPayment last_payment;
    private LastPayment last_bill;

    //fields used for graph response
    private GraphData trends;
    private DurationAverage duration_average;
    private TownshipAverage township_average;

    //common fields
    private String current_meter_reading;
    private String days_remaining;
    private String message;
    private boolean cutoff;

    //Chototel Response Fields
    private String consumed_units;
    private String total_units;
    private double consumed_amount = 0;
    private int days_left = 0;
    private double total_balance = 0;


    private boolean show_days_remaining = true;

    protected Bot(Parcel in) {
        mode = in.readString();
        balance = in.readString();
        total_available_days = in.readString();
        total_available_units = in.readString();
        units_remaining = in.readString();
        balance_remaining = in.readString();
        info_found = in.readByte() != 0;
        daily_average = in.readString();
        unbilled_amount = in.readString();
        units_consumed = in.readString();
        last_payment = in.readParcelable(LastPayment.class.getClassLoader());
        last_bill = in.readParcelable(LastPayment.class.getClassLoader());
        trends = in.readParcelable(GraphData.class.getClassLoader());
        duration_average = in.readParcelable(DurationAverage.class.getClassLoader());
        township_average = in.readParcelable(TownshipAverage.class.getClassLoader());
        current_meter_reading = in.readString();
        days_remaining = in.readString();
        message = in.readString();
        cutoff = in.readByte() != 0;
        consumed_units = in.readString();
        total_units = in.readString();
        consumed_amount = in.readDouble();
        days_left = in.readInt();
        total_balance = in.readDouble();
        show_days_remaining = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mode);
        dest.writeString(balance);
        dest.writeString(total_available_days);
        dest.writeString(total_available_units);
        dest.writeString(units_remaining);
        dest.writeString(balance_remaining);
        dest.writeByte((byte) (info_found ? 1 : 0));
        dest.writeString(daily_average);
        dest.writeString(unbilled_amount);
        dest.writeString(units_consumed);
        dest.writeParcelable(last_payment, flags);
        dest.writeParcelable(last_bill, flags);
        dest.writeParcelable(trends, flags);
        dest.writeParcelable(duration_average, flags);
        dest.writeParcelable(township_average, flags);
        dest.writeString(current_meter_reading);
        dest.writeString(days_remaining);
        dest.writeString(message);
        dest.writeByte((byte) (cutoff ? 1 : 0));
        dest.writeString(consumed_units);
        dest.writeString(total_units);
        dest.writeDouble(consumed_amount);
        dest.writeInt(days_left);
        dest.writeDouble(total_balance);
        dest.writeByte((byte) (show_days_remaining ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bot> CREATOR = new Creator<Bot>() {
        @Override
        public Bot createFromParcel(Parcel in) {
            return new Bot(in);
        }

        @Override
        public Bot[] newArray(int size) {
            return new Bot[size];
        }
    };

    public boolean isShow_days_remaining() {
        return show_days_remaining;
    }

    public void setShow_days_remaining(boolean show_days_remaining) {
        this.show_days_remaining = show_days_remaining;
    }

    public String getTotal_units() {
        return total_units;
    }

    public void setTotal_units(String total_units) {
        this.total_units = total_units;
    }

    public String getConsumed_units() {
        return consumed_units;
    }

    public void setConsumed_units(String consumed_units) {
        this.consumed_units = consumed_units;
    }

    public double getConsumed_amount() {
        return consumed_amount;
    }

    public void setConsumed_amount(double consumed_amount) {
        this.consumed_amount = consumed_amount;
    }

    public int getDays_left() {
        return days_left;
    }

    public void setDays_left(int days_left) {
        this.days_left = days_left;
    }

    public double getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(double total_balance) {
        this.total_balance = total_balance;
    }

    public boolean isCutoff() {
        return cutoff;
    }

    public void setCutoff(boolean cutoff) {
        this.cutoff = cutoff;
    }

    public LastPayment getLast_bill() {
        return last_bill;
    }

    public void setLast_bill(LastPayment last_bill) {
        this.last_bill = last_bill;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotal_available_days() {
        return total_available_days;
    }

    public void setTotal_available_days(String total_available_days) {
        this.total_available_days = total_available_days;
    }

    public String getTotal_available_units() {
        return total_available_units;
    }

    public void setTotal_available_units(String total_available_units) {
        this.total_available_units = total_available_units;
    }

    public String getUnits_remaining() {
        return units_remaining;
    }

    public void setUnits_remaining(String units_remaining) {
        this.units_remaining = units_remaining;
    }

    public String getBalance_remaining() {
        return balance_remaining;
    }

    public void setBalance_remaining(String balance_remaining) {
        this.balance_remaining = balance_remaining;
    }

    public boolean isInfo_found() {
        return info_found;
    }

    public void setInfo_found(boolean info_found) {
        this.info_found = info_found;
    }

    public String getDaily_average() {
        return daily_average;
    }

    public void setDaily_average(String daily_average) {
        this.daily_average = daily_average;
    }

    public String getUnbilled_amount() {
        return unbilled_amount;
    }

    public void setUnbilled_amount(String unbilled_amount) {
        this.unbilled_amount = unbilled_amount;
    }

    public String getUnits_consumed() {
        return units_consumed;
    }

    public void setUnits_consumed(String units_consumed) {
        this.units_consumed = units_consumed;
    }

    public LastPayment getLast_payment() {
        return last_payment;
    }

    public void setLast_payment(LastPayment last_payment) {
        this.last_payment = last_payment;
    }

    public GraphData getTrends() {
        return trends;
    }

    public void setTrends(GraphData trends) {
        this.trends = trends;
    }

    public DurationAverage getDuration_average() {
        return duration_average;
    }

    public void setDuration_average(DurationAverage duration_average) {
        this.duration_average = duration_average;
    }

    public TownshipAverage getTownship_average() {
        return township_average;
    }

    public void setTownship_average(TownshipAverage township_average) {
        this.township_average = township_average;
    }

    public String getCurrent_meter_reading() {
        return current_meter_reading;
    }

    public void setCurrent_meter_reading(String current_meter_reading) {
        this.current_meter_reading = current_meter_reading;
    }

    public String getDays_remaining() {
        return days_remaining;
    }

    public void setDays_remaining(String days_remaining) {
        this.days_remaining = days_remaining;
    }
}
