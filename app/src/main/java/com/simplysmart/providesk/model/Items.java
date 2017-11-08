package com.simplysmart.providesk.model;

/**
 * Created by chandrashekhar on 24/8/15.
 */
public class Items {

    private String title;
    private String item_new;
    private String icon;

    public Items() {

    }

    public Items(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getItem_new() {
        return item_new;
    }

    public void setItem_new(String item_new) {
        this.item_new = item_new;
    }
}
