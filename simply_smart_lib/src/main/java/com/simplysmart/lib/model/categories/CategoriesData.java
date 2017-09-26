package com.simplysmart.lib.model.categories;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 10/28/15.
 */
public class CategoriesData implements Parcelable {

    protected CategoriesData(Parcel in) {
        Complaint = in.createTypedArrayList(CategoryField.CREATOR);
        Request = in.createTypedArrayList(CategoryField.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Complaint);
        dest.writeTypedList(Request);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoriesData> CREATOR = new Creator<CategoriesData>() {
        @Override
        public CategoriesData createFromParcel(Parcel in) {
            return new CategoriesData(in);
        }

        @Override
        public CategoriesData[] newArray(int size) {
            return new CategoriesData[size];
        }
    };

    public ArrayList<CategoryField> getComplaint() {
        return Complaint;
    }

    public void setComplaint(ArrayList<CategoryField> complaint) {
        Complaint = complaint;
    }

    ArrayList<CategoryField> Complaint;

    public ArrayList<CategoryField> getRequest() {
        return Request;
    }

    public void setRequest(ArrayList<CategoryField> request) {
        Request = request;
    }

    ArrayList<CategoryField> Request;
}
