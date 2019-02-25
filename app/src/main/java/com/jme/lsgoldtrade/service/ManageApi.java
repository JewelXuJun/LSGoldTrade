package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ManageApi {

    @POST("/infoapi/v1/android/cmsSuggest/add")
    Call<DTResponse> feedback(@Body Map<String, String> map);

}
