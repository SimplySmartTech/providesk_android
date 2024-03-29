package com.simplysmart.lib.model.helpdesk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 12/8/15.
 */
public class ComplaintLists implements Parcelable {


    private String complaint_category_name;
    private String request_category_name;
    private String description;
    private String sub_category_name;
    private String number;
    private String unit_info;
    private String unread_comments;
    private String id;
    private String aasm_state;
    private String category_short_name;
    private String priority;


    protected ComplaintLists(Parcel in) {
        complaint_category_name = in.readString();
        request_category_name = in.readString();
        description = in.readString();
        sub_category_name = in.readString();
        number = in.readString();
        unit_info = in.readString();
        unread_comments = in.readString();
        id = in.readString();
        aasm_state = in.readString();
        category_short_name = in.readString();
        priority = in.readString();
    }

    public static final Creator<ComplaintLists> CREATOR = new Creator<ComplaintLists>() {
        @Override
        public ComplaintLists createFromParcel(Parcel in) {
            return new ComplaintLists(in);
        }

        @Override
        public ComplaintLists[] newArray(int size) {
            return new ComplaintLists[size];
        }
    };

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory_short_name() {
        return category_short_name;
    }

    public void setCategory_short_name(String category_short_name) {
        this.category_short_name = category_short_name;
    }

    public String getComplaint_category_name() {
        return complaint_category_name;
    }

    public void setComplaint_category_name(String complaint_category_name) {
        this.complaint_category_name = complaint_category_name;
    }

    public String getRequest_category_name() {
        return request_category_name;
    }

    public void setRequest_category_name(String request_category_name) {
        this.request_category_name = request_category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnit_info() {
        return unit_info;
    }

    public void setUnit_info(String unit_info) {
        this.unit_info = unit_info;
    }

    public String getUnread_comments() {
        return unread_comments;
    }

    public void setUnread_comments(String unread_comments) {
        this.unread_comments = unread_comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(complaint_category_name);
        parcel.writeString(request_category_name);
        parcel.writeString(description);
        parcel.writeString(sub_category_name);
        parcel.writeString(number);
        parcel.writeString(unit_info);
        parcel.writeString(unread_comments);
        parcel.writeString(id);
        parcel.writeString(aasm_state);
        parcel.writeString(category_short_name);
        parcel.writeString(priority);
    }
}
