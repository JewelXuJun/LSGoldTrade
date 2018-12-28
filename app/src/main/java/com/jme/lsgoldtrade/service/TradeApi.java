package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TradeApi {

    @GET("/gold-trade/v1.0.0/android/account")
    Call<DTResponse> account();

    @POST("/gold-trade/v1.0.0/android/inoutmoney")
    Call<DTResponse> inoutmoney(@Body Map<String, String> map);

}
