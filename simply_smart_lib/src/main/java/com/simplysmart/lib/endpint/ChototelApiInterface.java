package com.simplysmart.lib.endpint;

import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.walllet.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shekhar on 16/01/17.
 */
public interface ChototelApiInterface {

    //Show Bot Details (Meter details)
    @Headers("Cache-Control: public,max-age=600")
    @GET("/api/hms/utilities/info")
    Call<BotResponse> getBotDetails(@Query("unit_id") String unit_id,
                                    @Query("booking_id") String booking_id,
                                    @Query("type") String type,
                                    @Query("subdomain") String subDomain,
                                    @Query("site_id") String site_id);

    @GET("/api/hms/transactions")
    Call<TransactionResponse> getTransactions(@Query("booking_id") String booking_id,
                                              @Query("subdomain") String subDomain,
                                              @Query("site_id") String site_id);

    @GET("/api/hms/residents/{id}")
    Call<LoginResponse> getProfileInfo(@Path("id") String resident_id,
                                       @Query("subdomain") String subDomain,
                                       @Query("site_id") String site_id);
}
