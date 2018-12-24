package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.UserInfoVo;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class UserService extends IService<UserApi> {

    public UserService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static UserService getInstance() {
        return (UserService) getInstance(UserService.class);
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

    public API login = new API<UserInfoVo>("Login") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.login(params);
        }
    };

}
