package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.User;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class TradeService extends IService<TradeApi> {

    public TradeService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static TradeService getInstance() {
        return (TradeService) getInstance(TradeService.class);
    }

    protected Interceptor addHeader() {
        Interceptor interceptor = chain -> {
            Request request = chain.request();

            User user = User.getInstance();

            if (user.isLogin())
                request = request.newBuilder().addHeader("token", user.getToken()).build();
            else
                request = request.newBuilder().build();

            Response response = chain.proceed(request);

            return response;
        };

        return interceptor;
    }

    public API account = new API<AccountVo>("Account") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.account();
        }
    };

}
