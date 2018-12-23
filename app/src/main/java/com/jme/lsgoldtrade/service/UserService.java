package com.jme.lsgoldtrade.service;

import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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

}
