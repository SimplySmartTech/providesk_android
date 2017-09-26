package com.simplysmart.lib.model.categories;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 10/28/15.
 */
public class CategoryField implements Parcelable {

    private String name, id;

    private ArrayList<SubCategories> sub_categories;

    protected CategoryField(Parcel in) {
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

    public static final Creator<CategoryField> CREATOR = new Creator<CategoryField>() {
        @Override
        public CategoryField createFromParcel(Parcel in) {
            return new CategoryField(in);
        }

        @Override
        public CategoryField[] newArray(int size) {
            return new CategoryField[size];
        }
    };

    public ArrayList<SubCategories> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(ArrayList<SubCategories> sub_categories) {
        this.sub_categories = sub_categories;
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


}
