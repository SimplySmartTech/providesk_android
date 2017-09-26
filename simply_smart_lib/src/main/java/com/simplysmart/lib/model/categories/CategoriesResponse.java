package com.simplysmart.lib.model.categories;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 10/28/15.
 */
public class CategoriesResponse implements Parcelable {

    private CategoriesData categories;

    protected CategoriesResponse(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoriesResponse> CREATOR = new Creator<CategoriesResponse>() {
        @Override
        public CategoriesResponse createFromParcel(Parcel in) {
            return new CategoriesResponse(in);
        }

        @Override
        public CategoriesResponse[] newArray(int size) {
            return new CategoriesResponse[size];
        }
    };

    public CategoriesData getData() {
        return categories;
    }

    public void setData(CategoriesData categories) {
        this.categories = categories;
    }

}
