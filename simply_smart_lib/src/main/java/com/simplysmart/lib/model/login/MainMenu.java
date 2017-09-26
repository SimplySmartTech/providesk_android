package com.simplysmart.lib.model.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 23/11/16.
 */
public class MainMenu implements Parcelable {

    private String name;
    private boolean active;
    private int position;
    private String icon;


    protected MainMenu(Parcel in) {
        name = in.readString();
        active = in.readByte() != 0;
        position = in.readInt();
        icon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeInt(position);
        dest.writeString(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainMenu> CREATOR = new Creator<MainMenu>() {
        @Override
        public MainMenu createFromParcel(Parcel in) {
            return new MainMenu(in);
        }

        @Override
        public MainMenu[] newArray(int size) {
            return new MainMenu[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
