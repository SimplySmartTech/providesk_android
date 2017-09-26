package com.simplysmart.lib.endpint;


import com.simplysmart.lib.model.lodhachess.request.LodhaChessCancelRequestEnvelope;
import com.simplysmart.lib.model.lodhachess.request.LodhaChessRequestEnvelope;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessCancelResponseEnvelope;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by shekhar on 23/03/17.
 */
public interface LodhaChessApiInterface {

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/SendCardNo/VDPService.asmx?op=GiveQueueNoAndStatus")
    Call<LodhaChessResponseEnvelope> getQueueNoAndStatus(@Body LodhaChessRequestEnvelope body);


    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/SendCardNo/VDPService.asmx?op=GiveCancelRequest")
    Call<LodhaChessCancelResponseEnvelope> postCancelRequest(@Body LodhaChessCancelRequestEnvelope body);
}
