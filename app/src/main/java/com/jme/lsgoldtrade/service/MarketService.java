package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class MarketService extends IService<MarketApi> {

    public MarketService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static MarketService getInstance() {
        return (MarketService) getInstance(MarketService.class);
    }

    protected Interceptor addHeader() {
        Interceptor interceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            Request request = builder.build();
            Response response = chain.proceed(request);

            return response;
        };

        return interceptor;
    }

    /**
     * 实时行情接口
     */
    public API getRealTimeQuotes = new API<String>("GetRealTimeQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String type = params.get("type");
            String list = params.get("list");

            return mApi.getRealTimeQuotes(type, list);
        }
    };

    /**
     * 分时行情接口
     */
    public API getTChartQuotes = new API<String>("GetTChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String type = params.get("type");
            params.remove("type");

            return mApi.getTChartQuotes(type, params);
        }
    };

    /**
     * K线行情接口
     */
    public API getKChartQuotes = new API<String>("GetKChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String type = params.get("type");
            params.remove("type");

            return mApi.getKChartQuotes(type, params);
        }
    };

}
