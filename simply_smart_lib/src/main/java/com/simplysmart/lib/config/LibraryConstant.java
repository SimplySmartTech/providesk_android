package com.simplysmart.lib.config;

public class LibraryConstant {

    //Flag for debugging log mode
    public static boolean isDebuggable = true;

    //Timeout values
    public static final long READ_TIMEOUT = 60;
    public static final long CONNECTION_TIMEOUT = 60;

    //Api url component (Production)
//    public static final String PROTOCOL = "https://";
    public static final String DOMAIN = "api.simplysmart.tech";
//    public static final String DOMAIN = "township.xrbia.com";

    //Api url component (Local)
    public static final String PROTOCOL = "http://";
//    public static final String DOMAIN = "114.143.133.250:3000";

    public static final String SERVER_URL = PROTOCOL + DOMAIN;

    // Http request header content
    public static final String ACCEPT_TYPE = "application/vnd.simplysmart.v1+json";
    public static final String ACCEPT_TYPE_V2 = "application/vnd.simplysmart.v2+json";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CONTENT_LOCALE = "en-US";

    public static final String cloudinary_base_path = "https://res.cloudinary.com/mixtape/image/upload/";

}
