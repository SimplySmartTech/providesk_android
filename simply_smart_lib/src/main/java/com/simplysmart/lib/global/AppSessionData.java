package com.simplysmart.lib.global;

import android.app.Application;

/**
 * Created by shekhar on 13/5/15.
 * AppSessionData contains all the session related object & values for logged in user
 * Initialize as application level context.
 */
public class AppSessionData extends Application {

    private String authToken;
    private String api_key;
    private String subdomain;

    private String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public static AppSessionData mGlobalData;

    public static AppSessionData getInstance() {

        if (mGlobalData == null) {
            mGlobalData = new AppSessionData();
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


    public String getApiKey() {
        return this.api_key;
    }


}