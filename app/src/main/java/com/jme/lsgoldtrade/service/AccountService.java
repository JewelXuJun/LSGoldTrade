package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.domain.UserDetailsVo;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.domain.WithDraw;
import java.util.HashMap;
import java.util.List;
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

    public API accountDetailList = new API<List<UserDetailsVo>>("AccountDetailList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.accountDetailList(params);
        }
    };

    public API withdraw = new API<WithDraw>("Withdraw") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.withdraw(params);
        }
    };
}
