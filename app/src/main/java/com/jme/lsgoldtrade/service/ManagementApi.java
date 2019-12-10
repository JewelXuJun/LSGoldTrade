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

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/analystList")
    Call<DTResponse> analystList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/marketJudge/list")
    Call<DTResponse> marketJudgeList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/list")
    Call<TradingBoxResponse> getOrderList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/getDetailInfo")
    Call<DTResponse> getDetailInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/embeddedSingle/revocation")
    Call<DTResponse> revocation(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/protocol/hasProfitLossRiskSign")
    Call<DTResponse> hasProfitLossRiskSign(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/getGreeting")
    Call<DTResponse> getGreeting(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/list")
    Call<DTResponse> questTypeList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/customerService/answerList")
    Call<DTResponse> answerList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/questionType/getQuestionListByType")
    Call<DTResponse> getQuestionListByType(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/vote/add")
    Call<DTResponse> add(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/embeddedSingle/placeOrder")
    Call<DTResponse> placeOrder(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/openValueAddedServices")
    Call<DTResponse> openValueAddedServices(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/checkOrder")
    Call<DTResponse> checkOrder(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/timeLine/list")
    Call<DTResponse> timeLineList(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/timeLine/save")
    Call<DTResponse> timeLineSave(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getProtocolVersion")
    Call<DTResponse> getProtocolVersion(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/insertRatifyAccord")
    Call<DTResponse> insertRatifyAccord(@Body Map<String, Object> map);

    @GET("/tjsmanage/infoapi/v1/android/getListExt")
    Call<DTResponse> getListExt(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/setAppSubscribe")
    Call<DTResponse> setAppSubscribe(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/getBoxInfo")
    Call<DTResponse> getBoxInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/queryTradeBoxList")
    Call<DTResponse> queryTradeBoxList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/queryTradeBoxLossHistoryInfo")
    Call<DTResponse> queryTradeBoxLossHistoryInfo(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/tradeBoxByTradeId")
    Call<DTResponse> getTradeBoxByTradeId(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradingBox/querySubscriberCount")
    Call<DTResponse> querySubscriberCount(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/userFund/accountDetailList")
    Call<DTResponse> accountDetailList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradeSafety/getOnlineTimeList")
    Call<DTResponse> getOnlineTimeList(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/tradeSafety/setUserOnlineTime")
    Call<DTResponse> setUserOnlineTime(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradeSafety/getUserOnlineTime")
    Call<DTResponse> getUserOnlineTime(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradeSafety/getUserPasswordSettingInfo")
    Call<DTResponse> getUserPasswordSettingInfo(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/tradeSafety/updatePasswordOpenStatus")
    Call<DTResponse> updatePasswordOpenStatus(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/tradeSafety/setTradePassword")
    Call<DTResponse> setTradePassword(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/tradeSafety/sendMessage")
    Call<DTResponse> sendMessage(@QueryMap Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/tradeSafety/validateLoginPassword")
    Call<DTResponse> validateLoginPassword(@Body Map<String, String> map);

    @POST("/tjsmanage/infoapi/v1/android/tradeSafety/unlockTradePassword")
    Call<DTResponse> unlockTradePassword(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getPayIcon")
    Call<DTResponse> getPayIcon(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/getCustomerArrearage")
    Call<DTResponse> getCustomerArrearage(@QueryMap Map<String, String> map);
}
