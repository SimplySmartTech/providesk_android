package com.simplysmart.lib.model.bots;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 27/10/15.
 */
public class BotData implements Parcelable {

    private Bot utility;

    protected BotData(Parcel in) {
        utility = in.readParcelable(Bot.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(utility, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BotData> CREATOR = new Creator<BotData>() {
        @Override
        public BotData createFromParcel(Parcel in) {
            return new BotData(in);
        }

        @Override
        public BotData[] newArray(int size) {
            return new BotData[size];
        }
    };

    public Bot getBot() {
        return utility;
    }

    public void setBot(Bot utility) {
        this.utility = utility;
    }
}
