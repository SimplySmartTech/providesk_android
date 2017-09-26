package com.simplysmart.lib.endpint;

import com.simplysmart.lib.model.walllet.TransactionResponse;
import com.simplysmart.lib.model.walllet.WalletCredentialRequest;
import com.simplysmart.lib.model.walllet.WalletCredentialResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shekhar on 1/02/17.
 */
public interface WalletApiInterface {

    @GET("/api/transactions")
    Call<TransactionResponse> getTransactions(@Query("resident_id") String resident_id,

                                              @Query("subdomain") String subDomain,
                                              @Query("site_id") String site_id);

    @POST("/api/transactions")
    Call<WalletCredentialResponse> getTransactionCredentials(@Query("subdomain") String subDomain,
                                                             @Query("site_id") String site_id,
                                                             @Body WalletCredentialRequest request);


    @PUT("/api/transactions/{transaction_id}")
    Call<TransactionResponse> updateTransaction(@Path("transaction_id") String transactionId,
                                                @Query("subdomain") String subDomain,
                                                @Query("site_id") String site_id,
                                                @Body WalletCredentialRequest request);

}
