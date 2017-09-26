package com.simplysmart.lib.model.categories;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 10/28/15.
 */
public class SubCategories implements Parcelable {

    private String name, id;

    protected SubCategories(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategories> CREATOR = new Creator<SubCategories>() {
        @Override
        public SubCategories createFromParcel(Parcel in) {
            return new SubCategories(in);
        }

        @Override
        public SubCategories[] newArray(int size) {
            return new SubCategories[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }
}
