package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TradeApi {

    @GET("/gold-trade/v1.0.0/android/account")
    Call<DTResponse> account();

}
