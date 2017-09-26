package com.simplysmart.lib.model.bots;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 9/12/15.
 */
public class BotStatus implements Parcelable {


    private String message;
    private String cutoff;
    private String bot_alive;
    private String current_reading;

    protected BotStatus(Parcel in) {
        message = in.readString();
        cutoff = in.readString();
        bot_alive = in.readString();
        current_reading = in.readString();
    }

    public static final Creator<BotStatus> CREATOR = new Creator<BotStatus>() {
        @Override
        public BotStatus createFromParcel(Parcel in) {
            return new BotStatus(in);
        }

        @Override
        public BotStatus[] newArray(int size) {
            return new BotStatus[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCutoff() {
        return cutoff;
    }

    public void setCutoff(String cutoff) {
        this.cutoff = cutoff;
    }

    public String getBot_alive() {
        return bot_alive;
    }

    public void setBot_alive(String bot_alive) {
        this.bot_alive = bot_alive;
    }

    public String getCurrent_reading() {
        return current_reading;
    }

    public void setCurrent_reading(String current_reading) {
        this.current_reading = current_reading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(cutoff);
        parcel.writeString(bot_alive);
        parcel.writeString(current_reading);
    }
}
