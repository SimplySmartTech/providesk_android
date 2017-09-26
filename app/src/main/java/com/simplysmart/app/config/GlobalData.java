package com.simplysmart.app.config;

import android.app.Application;

import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.MainMenu;
import com.simplysmart.lib.model.login.Unit;

import java.util.ArrayList;

/**
 * Created by shekhar on 17/8/15.
 */
public class GlobalData extends Application {

    public static GlobalData mGlobalData;

    private String authToken;
    private String api_key;
    private String residentId;

    private String bookingId;

    private boolean isChanged = false;
    private String updatedUnitName = "";
    private String demoUnitId = "";

    private ArrayList<Unit> units;

    private ArrayList<MainMenu> menus;

    private AccessPolicy accessPolicy;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public ArrayList<MainMenu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<MainMenu> menus) {
        this.menus = menus;
    }

    public AccessPolicy getAccessPolicy() {
        return accessPolicy;
    }

    public void setAccessPolicy(AccessPolicy accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    public String getDemoUnitId() {
        return demoUnitId;
    }

    public void setDemoUnitId(String demoUnitId) {
        this.demoUnitId = demoUnitId;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public String getUpdatedUnitName() {
        return updatedUnitName;
    }

    public void setUpdatedUnitName(String updatedUnitName) {
        this.updatedUnitName = updatedUnitName;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }


    public static GlobalData getInstance() {

        if (mGlobalData == null) {
            mGlobalData = new GlobalData();
        }
        return mGlobalData;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setApiKey(String api_key) {

        this.api_key = api_key;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getResidentId() {
        return residentId;
    }

    public String getApiKey() {
        return this.api_key;
    }


}