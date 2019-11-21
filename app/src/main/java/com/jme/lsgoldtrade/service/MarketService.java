package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.common.util.KChartVo;
import com.jme.common.util.TChartVo;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.DetailVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.SectionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;

import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class MarketService extends IService<MarketApi> {

    public MarketService() {
        super(Constants.HttpConst.URL_BASE_MARKET, true);
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
     * 分笔明细
     */
    public API getDetail = new API<List<DetailVo>>("GetDetail") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.getDetail(params);
        }
    };

    /**
     * 合约交易节
     */
    public API getContractSection = new API<List<SectionVo>>("GetContractSection") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.getContractSection(params);
        }
    };

    /**
     * 分时行情接口
     */
    public API getTChartQuotes = new API<List<TChartVo>>("GetTChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.getTChartQuotes(params);
        }
    };

    /**
     * K线行情接口
     */
    public API getKChartQuotes = new API<List<KChartVo>>("GetKChartQuotes") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String type = params.get("type");

            return mApi.getKChartQuotes(type, params);
        }
    };

    public API queryQuotation = new API<TenSpeedVo>("QueryQuotation") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String contractId = params.get("contractId");

            return mApi.queryQuotation(contractId);
        }
    };

}
