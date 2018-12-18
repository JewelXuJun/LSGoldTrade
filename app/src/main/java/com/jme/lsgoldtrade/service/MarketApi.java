package com.jme.lsgoldtrade.service;

import com.jme.common.network.DTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MarketApi {

    /**
     * 五档实时行情接口
     *
     * @param list 合约列表如果有多个用逗号分隔
     * @return
     */
    @GET("/gold-hq/v1.0.0/android/quotes5")
    Call<DTResponse> getFiveSpeedQuotes(@Query("list") String list);

    /**
     * 十档实时行情接口
     *
     * @param list 合约列表如果有多个用逗号分隔
     * @return
     */
    @GET("/gold-hq/v1.0.0/android/quotes10")
    Call<DTResponse> getTenSpeedQuotes(@Query("list") String list);

    /**
     * 分笔明细
     *
     * @return
     */
    @GET("/gold-hq/v1.0.0/android/detail")
    Call<DTResponse> getDetail(@QueryMap Map<String, String> map);

    /**
     * 分时行情接口
     *
     * @return
     */
    @GET("/gold-hq/v1.0.0/android/time1")
    Call<DTResponse> getTChartQuotes(@QueryMap Map<String, String> map);

    /**
     * K线行情接口
     *
     * @param type 1minK 1分钟K线、15minK 15分钟K线、30minK 30分钟K线、60minK 60分钟K线、dayK 日K线、weekK 周K线、monthK 月K线
     * @return
     */
    @GET("/gold-hq/v1.0.0/android/{type}")
    Call<DTResponse> getKChartQuotes(@Path("type") String type, @QueryMap Map<String, String> map);

}
