package com.jme.lsgoldtrade.service;

import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TradeService extends IService<TradeApi> {

    public TradeService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static TradeService getInstance() {
        return (TradeService) getInstance(TradeService.class);
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

}
