package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.KChartVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.TChartVo;

import java.util.HashMap;
import java.util.List;

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
     * 五档实时行情接口
     */
    public API getFiveSpeedQuotes = new API<List<FiveSpeedVo>>("GetFiveSpeedQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String list = params.get("list");

            return mApi.getFiveSpeedQuotes(list);
        }
    };

    /**
     * 十档实时行情接口
     */
    public API getTenSpeedQuotes = new API<List<TenSpeedVo>>("GetTenSpeedQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String list = params.get("list");

            return mApi.getTenSpeedQuotes(list);
        }
    };

    /**
     * 分时行情接口
     */
    public API getTChartQuotes = new API<TChartVo>("GetTChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.getTChartQuotes(params);
        }
    };

    /**
     * K线行情接口
     */
    public API getKChartQuotes = new API<KChartVo>("GetKChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String type = params.get("type");
            params.remove("type");

            return mApi.getKChartQuotes(type, params);
        }
    };

}
