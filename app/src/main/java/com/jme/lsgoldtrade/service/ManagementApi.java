package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ManagementApi {

    @POST("/tjsmanage/infoapi/v1/android/cmsSuggest/add")
    Call<DTResponse> feedback(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsAdsenseItems/allList")
    Call<DTResponse> bannerAllList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsContent/firstThree")
    Call<DTResponse> firstThree(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/study")
    Call<DTResponse> study(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsChannel/allList")
    Call<DTResponse> channelAllList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsComtent/list")
    Call<DTResponse> channelList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getVersionInfo")
    Call<DTResponse> getVersionInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/strategy/list")
    Call<DTResponse> strategy(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/userNavigationModule/list")
    Call<DTResponse> navigatorList(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/userNavigationModule/save")
    Call<DTResponse> saveNavigatorList(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxHomedataInfo")
    Call<DTResponse> tradingBox(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/setSubscribe")
    Call<DTResponse> subscribe(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxByTradeId")
    Call<DTResponse> tradingBoxDetails(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/getBoxInfo")
    Call<DTResponse> tradingBoxInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxHistoryInfo")
    Call<DTResponse> historyBox(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/analystList")
    Call<DTResponse> fenxishi(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/list")
    Call<DTResponse> fenxishiList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getListExt")
    Call<DTResponse> isSubscribe(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/list")
    Call<DTResponse> myOrder(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/getDetailInfo")
    Call<DTResponse> orderDetails(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/revocation")
    Call<DTResponse> revocation(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/protocol/hasProfitLossRiskSign")
    Call<DTResponse> hasProfitLossRiskSign(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/protocol/hasProfitLossRiskSign")
    Call<DTResponse> diyanfangxiang(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/getGreeting")
    Call<DTResponse> welcome(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/list")
    Call<DTResponse> questTypeList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/answerList")
    Call<DTResponse> ask(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/getQuestionListByType")
    Call<DTResponse> getQuestionListByType(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getMaxTradeNum")
    Call<DTResponse> getMaxTradeNum(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getUserAddedServicesStatus")
    Call<DTResponse> getUserAddedServicesStatus(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/protocol/agreeProfitLossRisk")
    Call<DTResponse> agree(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/setAppSubscribe")
    Call<DTResponse> setSubscribe(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/vote/add")
    Call<DTResponse> rate(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/embeddedSingle/placeOrder")
    Call<DTResponse> submitTradingBox(@Body Map<String, String> map);

}
