package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TradeApi {

    @GET("/gold-trade/v1.0.0/android/account")
    Call<DTResponse> account(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/inoutmoney")
    Call<DTResponse> inoutmoney(@Body Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/transpage")
    Call<DTResponse> transpage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/dailystatement")
    Call<DTResponse> dailystatement(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/orderpage")
    Call<DTResponse> orderpage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/orderhispage")
    Call<DTResponse> orderhispage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/dealpage")
    Call<DTResponse> dealpage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/dealhispage")
    Call<DTResponse> dealhispage(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/position")
    Call<DTResponse> position(@QueryMap Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/contractInfo")
    Call<DTResponse> contractInfo(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/limitOrder")
    Call<DTResponse> limitOrder(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/marketOrder")
    Call<DTResponse> marketOrder(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/revocateorder")
    Call<DTResponse> revocateorder(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/minReserveFund")
    Call<DTResponse> minReserveFund(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/setWarn")
    Call<DTResponse> setWarm(@Body Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/warnInfo")
    Call<DTResponse> warnInfo(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/icbcMsg")
    Call<DTResponse> icbcMsg(@Body Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/registerMsg")
    Call<DTResponse> registerMsg(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/verifyIdCard")
    Call<DTResponse> verifyIdCard(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/registerLogin")
    Call<DTResponse> registerLogin(@Body Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/resetLoginPasswordMsg")
    Call<DTResponse> resetLoginPasswordMsg(@QueryMap Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/resetLoginPassword")
    Call<DTResponse> resetLoginPassword(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/bindAccount")
    Call<DTResponse> bindaccount(@Body Map<String, String> map);

    @POST("/gold-trade/v1.0.0/android/setLoginPassword")
    Call<DTResponse> setLoginPassword(@Body Map<String, String> map);

    @GET("/gold-trade/v1.0.0/android/whetherIdCard")
    Call<DTResponse> whetherIdCard(@QueryMap Map<String, String> map);
}
