package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;
import com.jme.lsgoldtrade.domain.TradingBoxResponse;

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

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/listInPage")
    Call<DTResponse> listInPage(@QueryMap Map<String, String> map);

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
    Call<DTResponse> tradeBoxHomedataInfo(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/setSubscribe")
    Call<DTResponse> subscribe(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxByTradeId")
    Call<DTResponse> tradeBoxByTradeId(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/getBoxInfo")
    Call<DTResponse> getBoxInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxHistoryInfo")
    Call<DTResponse> tradeBoxHistoryInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/analystList")
    Call<DTResponse> analystList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/list")
    Call<DTResponse> marketJudgeList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getListExt")
    Call<DTResponse> getListExt(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/list")
    Call<TradingBoxResponse> getOrderList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/getDetailInfo")
    Call<DTResponse> getDetailInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/revocation")
    Call<DTResponse> revocation(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/protocol/hasProfitLossRiskSign")
    Call<DTResponse> hasProfitLossRiskSign(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/protocol/hasProfitLossRiskSign")
    Call<DTResponse> diyanfangxiang(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/getGreeting")
    Call<DTResponse> getGreeting(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/list")
    Call<DTResponse> questTypeList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/answerList")
    Call<DTResponse> answerList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/getQuestionListByType")
    Call<DTResponse> getQuestionListByType(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getUserAddedServicesStatus")
    Call<DTResponse> getUserAddedServicesStatus(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/protocol/agreeProfitLossRisk")
    Call<DTResponse> agree(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/setAppSubscribe")
    Call<DTResponse> setAppSubscribe(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/vote/add")
    Call<DTResponse> add(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/embeddedSingle/placeOrder")
    Call<DTResponse> placeOrder(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/openValueAddedServices")
    Call<DTResponse> openValueAddedServices(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getStatus")
    Call<DTResponse> getStatus(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/ios/checkOrder")
    Call<DTResponse> checkOrder(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/ios/timeLine/list")
    Call<DTResponse> timeLineList(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/ios/timeLine/save")
    Call<DTResponse> timeLineSave(@Body Map<String, String> map);

}
