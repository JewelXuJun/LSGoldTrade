package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.DailyStatementVo;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
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

    public API inoutmoney = new API<String>("InoutMoney") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.inoutmoney(params);
        }
    };

    public API transpage = new API<InOutTurnOverVo>("Transpage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.transpage(params);
        }
    };

    public API dailystatement = new API<DailyStatementVo>("DailyStatement") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dailystatement(params);
        }
    };

    public API orderpage = new API<OrderPageVo>("OrderPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.orderpage(params);
        }
    };

    public API dealpage = new API<DealPageVo>("DealPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dealpage(params);
        }
    };

    public API position = new API<PositionPageVo>("Position") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.position(params);
        }
    };

}
