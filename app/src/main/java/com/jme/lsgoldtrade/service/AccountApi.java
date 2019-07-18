package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface AccountApi {

    @POST("/fundAccount/api/v1/android/userFund/withdraw")
    Call<DTResponse> withdraw(@Body Map<String, String> map);

    @GET("/fundAccount/api/v1/android/userInfo/getUserInfo")
    Call<DTResponse> getUserInfo(@QueryMap Map<String, String> map);

    @GET("/fundAccount/api/v1/android/openValueAddedServices")
    Call<DTResponse> openValueAddedServices(@QueryMap Map<String, String> map);

    @GET("/fundAccount/api/v1/android/userFund/accountDetailList")
    Call<DTResponse> accountDetailList(@QueryMap Map<String, String> map);
}
