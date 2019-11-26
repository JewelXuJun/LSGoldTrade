package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;
import com.jme.lsgoldtrade.domain.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserApi {

    @GET("/gold-trade/v1.0.0/android/kaptcha")
    Call<DTResponse> kaptcha();

    @GET("/gold-trade/v1.0.0/android/loginMsg")
    Call<DTResponse> loginMsg(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/login")
    Call<LoginResponse> login(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/logout")
    Call<DTResponse> logout(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/queryLoginResult")
    Call<LoginResponse> queryLoginResult(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/noticepage")
    Call<DTResponse> noticepage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/noticedetail")
    Call<DTResponse> noticedetail(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/syntime")
    Call<DTResponse> syntime(@QueryMap Map<String, String> map);

}
