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
     * 实时行情接口
     *
     * @param type quotes5 五档、quotes10 十档
     * @param list 合约列表如果有多个用逗号分隔
     * @return
     */
    @GET("/gold-hq/v1/android/{type}")
    Call<DTResponse> getRealTimeQuotes(@Path("type") String type, @Query("list") String list);

    /**
     * 分时行情接口
     *
     * @param type time1 1分钟、time5 5分钟、time15 15分钟
     * @return
     */
    @GET("/gold-hq/v1/android/{type}")
    Call<DTResponse> getTChartQuotes(@Path("type") String type, @QueryMap Map<String, String> map);

    /**
     * K线行情接口
     *
     * @param type 1minK 1分钟K线、15minK 15分钟K线、30minK 30分钟K线、60minK 60分钟K线、dayK 日K线、weekK 周K线、monthK 月K线
     * @return
     */
    @GET("/gold-hq/v1/android/{type}")
    Call<DTResponse> getKChartQuotes(@Path("type") String type, @QueryMap Map<String, String> map);

}
