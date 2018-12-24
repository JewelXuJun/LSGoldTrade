package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserApi {

    @POST("/gold-trade/v1.0.0/android/login")
    Call<DTResponse> login(@QueryMap Map<String, String> map);

}
