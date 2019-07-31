package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import java.util.HashMap;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class PaymentService extends IService<PaymentApi> {

    public PaymentService() {
        super(Constants.HttpConst.URL_BASE_ACCOUNT, true);
    }

    public static PaymentService getInstance() {
        return (PaymentService) getInstance(PaymentService.class);
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

    public API getTradeAppPayResponse = new API<String>("GetTradeAppPayResponse") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getTradeAppPayResponse(params);
        }
    };

    public API wechatPay = new API<WechatPayVo>("WechatPay") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.wechatPay(params);
        }
    };

    public API withdrawApply = new API<String>("WithdrawApply") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.withdrawApply(params);
        }
    };

}
