package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 17/8/15.
 */
public class Resident implements Parcelable {

    private String id;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String lang;
    private String api_key;
    private String auth_token;
    private String checkin_date;
    private String checkout_date;

    private String current_password;
    private String password;
    private String password_confirmation;

    private ArrayList<Unit> units;
    private ArrayList<Unit> sites;
    private String booking_id;

    private boolean active = false;

    private String profile_photo_url;

    public Resident() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected Resident(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
        address = in.readString();
        lang = in.readString();
        api_key = in.readString();
        auth_token = in.readString();
        checkin_date = in.readString();
        checkout_date = in.readString();
        current_password = in.readString();
        password = in.readString();
        password_confirmation = in.readString();
        units = in.createTypedArrayList(Unit.CREATOR);
        sites = in.createTypedArrayList(Unit.CREATOR);
        booking_id = in.readString();
        active = in.readByte() != 0;
        profile_photo_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(address);
        dest.writeString(lang);
        dest.writeString(api_key);
        dest.writeString(auth_token);
        dest.writeString(checkin_date);
        dest.writeString(checkout_date);
        dest.writeString(current_password);
        dest.writeString(password);
        dest.writeString(password_confirmation);
        dest.writeTypedList(units);
        dest.writeTypedList(sites);
        dest.writeString(booking_id);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(profile_photo_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Resident> CREATOR = new Creator<Resident>() {
        @Override
        public Resident createFromParcel(Parcel in) {
            return new Resident(in);
        }

        @Override
        public Resident[] newArray(int size) {
            return new Resident[size];
        }
    };

    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCheckin_date() {
        return checkin_date;
    }

    public void setCheckin_date(String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public ArrayList<Unit> getSites() {
        return sites;
    }

    public void setSites(ArrayList<Unit> sites) {
        this.sites = sites;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }


}
