package com.simplysmart.lib.request;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.ErrorUtils;
import com.simplysmart.lib.config.ServiceGeneratorLodhaChess;
import com.simplysmart.lib.endpint.LodhaChessApiInterface;
import com.simplysmart.lib.model.common.APIError;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessCancelRequestBody;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessCancelRequestData;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessCancelRequestEnvelope;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessRequestBody;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessRequestData;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessRequestEnvelope;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessCancelResponseEnvelope;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessResponseEnvelope;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shekhar on 23/03/17.
 */
public class LodhaChessCreateRequest extends CreateRequest {

    private static LodhaChessCreateRequest createRequest = new LodhaChessCreateRequest();

    private LodhaChessCreateRequest() {
        super();
    }

    public static LodhaChessCreateRequest getInstance() {
        return createRequest;
    }

    public void getQueueNoAndStatus(final String carNumber, final ApiCallback<LodhaChessResponseEnvelope> callback) {

        LodhaChessRequestEnvelope envelope = new LodhaChessRequestEnvelope();
        LodhaChessRequestBody body = new LodhaChessRequestBody();
        LodhaChessRequestData data = new LodhaChessRequestData();

        data.setCarNo(carNumber);
        body.setGiveQueueNoAndStatus(data);
        envelope.setBody(body);

        LodhaChessApiInterface apiInterface = ServiceGeneratorLodhaChess.providesApi();
        Call<LodhaChessResponseEnvelope> responseCall = apiInterface.getQueueNoAndStatus(envelope);

        responseCall.enqueue(new Callback<LodhaChessResponseEnvelope>() {
            @Override
            public void onResponse(Call<LodhaChessResponseEnvelope> call, final Response<LodhaChessResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LodhaChessResponseEnvelope> call, Throwable t) {
            }
        });
    }

    public void postCancelRequest(String carNumber, final ApiCallback<LodhaChessCancelResponseEnvelope> callback) {

        LodhaChessCancelRequestEnvelope envelope = new LodhaChessCancelRequestEnvelope();
        LodhaChessCancelRequestBody body = new LodhaChessCancelRequestBody();
        LodhaChessCancelRequestData data = new LodhaChessCancelRequestData();

        data.setCarNo(carNumber);
        body.setGiveCancelRequest(data);
        envelope.setBody(body);

        LodhaChessApiInterface apiInterface = ServiceGeneratorLodhaChess.providesApi();
        Call<LodhaChessCancelResponseEnvelope> responseCall = apiInterface.postCancelRequest(envelope);

        responseCall.enqueue(new Callback<LodhaChessCancelResponseEnvelope>() {
            @Override
            public void onResponse(Call<LodhaChessCancelResponseEnvelope> call, final Response<LodhaChessCancelResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LodhaChessCancelResponseEnvelope> call, Throwable t) {
            }
        });
    }

}
