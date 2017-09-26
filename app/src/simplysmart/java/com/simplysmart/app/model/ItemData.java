package com.simplysmart.app.model;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by shekhar on 7/9/16.
 */
public class ItemData {

    private Integer image;
    private String title;
    private Intent redirectIntent;

    public ItemData(Integer image, String title, @Nullable Intent redirectIntent) {
        this.image = image;
        this.title = title;
        this.redirectIntent = redirectIntent;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getRedirectIntent() {
        return redirectIntent;
    }

    public void setRedirectIntent(Intent redirectIntent) {
        this.redirectIntent = redirectIntent;
    }

}
