package com.simplysmart.lib.endpint;


import com.simplysmart.lib.model.bots.BotStatus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shekhar on 9/12/15.
 */
public interface DemoApiInterface {

    @GET("/api/demo/{unit_id}/{bot_type}/{request_for}/")
    Call<BotStatus> botStatus(@Path("unit_id") String unit_id,
                              @Path("bot_type") String bot_type,
                              @Path("request_for") String request_for,
                              @Query("subdomain") String subDomain,
                              @Query("site_id") String site_id);
}
