package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface WithholdAccountApi {

    @GET("/bankAccount/api/v1/android/withhold/getBanks")
    Call<DTResponse> getBanks(@QueryMap Map<String, String> map);

    @POST("/bankAccount/api/v1/android/withhold/sendSignMessage")
    Call<DTResponse> sendSignMessage(@Body Map<String, String> map);

    @POST("/bankAccount/api/v1/android/withhold/sign")
    Call<DTResponse> sign(@Body Map<String, String> map);

    @GET("/bankAccount/api/v1/android/withhold/getCustomerSignBankList")
    Call<DTResponse> getCustomerSignBankList(@QueryMap Map<String, String> map);
}
