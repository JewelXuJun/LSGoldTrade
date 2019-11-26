package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/24 17:09
 * Desc   : description
 */
public interface PaymentApi {

    @POST("/payment/api/v1/android/aliPay/getTradeAppPayResponse")
    Call<DTResponse> getTradeAppPayResponse(@Body Map<String, String> map);

    @POST("/payment/api/v1/android/wxPay/wechatPay")
    Call<DTResponse> wechatPay(@Body Map<String, String> map);

    @POST("/payment/api/v1/android/wxPay/withdrawApply")
    Call<DTResponse> withdrawApply(@Body Map<String, String> map);

    @POST("/payment/api/v1/android/wxPay/goldGoodsPay")
    Call<DTResponse> goldGoodsPay(@Body Map<String, String> map);

}
