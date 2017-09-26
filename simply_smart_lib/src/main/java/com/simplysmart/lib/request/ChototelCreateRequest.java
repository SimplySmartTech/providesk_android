package com.simplysmart.lib.request;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.ErrorUtils;
import com.simplysmart.lib.config.ServiceGenerator;
import com.simplysmart.lib.endpint.ChototelApiInterface;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.common.APIError;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.walllet.TransactionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shekhar on 16/01/17.
 * CreateRequest class offer all the methods to prepare request for network call & sync with server
 */
public class ChototelCreateRequest extends CreateRequest {

    private static ChototelCreateRequest createRequest = new ChototelCreateRequest();

    private ChototelCreateRequest() {
        super();
    }

    public static ChototelCreateRequest getInstance() {
        return createRequest;
    }

    public void getBotDetails(String unitId, String bookingId, String botType, final ApiCallback<BotResponse> callback) {

        ChototelApiInterface apiInterface = ServiceGenerator.createService(ChototelApiInterface.class);
        Call<BotResponse> responseCall = apiInterface.getBotDetails(unitId,
                bookingId,
                botType,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<BotResponse>() {

            @Override
            public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<BotResponse> call, Throwable t) {

            }
        });
    }

    public void getTransactions(String bookingId, final ApiCallback<TransactionResponse> callback) {

        ChototelApiInterface apiInterface = ServiceGenerator.createService(ChototelApiInterface.class);
        Call<TransactionResponse> responseCall = apiInterface.getTransactions(bookingId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<TransactionResponse>() {

            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {

            }
        });
    }

    public void getProfileInfo(String residentId, final ApiCallback<LoginResponse> callback) {

        ChototelApiInterface apiInterface = ServiceGenerator.createService(ChototelApiInterface.class);
        Call<LoginResponse> responseCall = apiInterface.getProfileInfo(
                residentId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

}
