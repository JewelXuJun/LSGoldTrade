package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.domain.WithDraw;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class AccountService extends IService<AccountApi> {

    public AccountService() {
        super(Constants.HttpConst.URL_BASE_ACCOUNT, true);
    }

    public static AccountService getInstance() {
        return (AccountService) getInstance(AccountService.class);
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

    public API getUserInfo = new API<UsernameVo>("GetUserInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getUserInfo(params);
        }
    };

    public API withdraw = new API<WithDraw>("Withdraw") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.withdraw(params);
        }
    };

    public API hasWeChatWithdrawAuth = new API<String>("HasWeChatWithdrawAuth") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.hasWeChatWithdrawAuth(params);
        }
    };

    public API sendVerifyCode = new API<String>("SendVerifyCode") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.sendVerifyCode(params);
        }
    };

    public API checkVerifyCode = new API<String>("CheckVerifyCode") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.checkVerifyCode(params);
        }
    };

    public API getWithdrawFeeRate = new API<String>("GetWithdrawFeeRate") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getWithdrawFeeRate(params);
        }
    };
}
