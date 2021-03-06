package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;
import com.jme.lsgoldtrade.domain.ConditionSheetResponse;
import com.jme.lsgoldtrade.domain.QuerySetStopOrderResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ConditionApi {

    @POST("/order/api/v1/android/conditionOrder/entrustConditionOrder")
    Call<DTResponse> entrustConditionOrder(@Body Map<String, Object> map);

    @GET("/order/api/v1/android/conditionOrder/queryConditionOrderById")
    Call<QuerySetStopOrderResponse> queryConditionOrderById(@QueryMap Map<String, Object> map);

    @GET("/order/api/v1/android/conditionOrder/queryConditionOrderPage")
    Call<ConditionSheetResponse> queryConditionOrderPage(@QueryMap Map<String, String> map);

    @GET("/order/api/v1/android/conditionOrder/queryConditionOrderRun")
    Call<DTResponse> queryConditionOrderRun(@QueryMap Map<String, String> map);

    @GET("/order/api/v1/android/conditionOrder/querySetStopOrder")
    Call<QuerySetStopOrderResponse> querySetStopOrder(@QueryMap Map<String, String> map);

    @GET("/order/api/v1/android/conditionOrder/revokeConditionOrder")
    Call<DTResponse> revokeConditionOrder(@QueryMap Map<String, String> map);

    @POST("/order/api/v1/android/conditionOrder/updateConditionOrder")
    Call<DTResponse> updateConditionOrder(@Body Map<String, Object> map);

}
