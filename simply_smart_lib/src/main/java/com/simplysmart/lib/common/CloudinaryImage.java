package com.simplysmart.lib.common;

import android.net.Uri;
import android.util.Log;

import com.simplysmart.lib.config.LibraryConstant;


/**
 * Created by okarin on 12/23/15.
 */
public class CloudinaryImage {

    public static String transform(String url, String transform_params){


        if(url != null && url.startsWith("https://res.cloudinary.com")) {
            Uri uri = Uri.parse(url);
            String token = uri.getLastPathSegment();
            return LibraryConstant.cloudinary_base_path + transform_params + "/" + token;
        }
        Log.v("class: CloudinaryImage", "image" + url);
        return url;
    }
}
