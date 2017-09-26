package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shekhar on 12/08/16.
 */
public class AccessPolicy implements Parcelable {

    private String company_type;
    private String background;
    private String logo_url;

    private ArrayList<MainMenu> menu;

    protected AccessPolicy(Parcel in) {
        company_type = in.readString();
        background = in.readString();
        logo_url = in.readString();
        menu = in.createTypedArrayList(MainMenu.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(company_type);
        dest.writeString(background);
        dest.writeString(logo_url);
        dest.writeTypedList(menu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccessPolicy> CREATOR = new Creator<AccessPolicy>() {
        @Override
        public AccessPolicy createFromParcel(Parcel in) {
            return new AccessPolicy(in);
        }

        @Override
        public AccessPolicy[] newArray(int size) {
            return new AccessPolicy[size];
        }
    };

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public ArrayList<MainMenu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MainMenu> menu) {
        this.menu = menu;
    }
}
