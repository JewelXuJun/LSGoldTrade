package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ManagementApi {

    @POST("/tjsmanage//infoapi/v1/android/cmsSuggest/add")
    Call<DTResponse> feedback(@Body Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsAdsenseItems/allList")
    Call<DTResponse> bannerAllList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/study")
    Call<DTResponse> study(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsChannel/allList")
    Call<DTResponse> channelAllList(@QueryMap Map<String, String> map);

    @GET("/tjsmanage/infoapi/v1/android/cmsComtent/list")
    Call<DTResponse> channelList(@QueryMap Map<String, String> map);

}
