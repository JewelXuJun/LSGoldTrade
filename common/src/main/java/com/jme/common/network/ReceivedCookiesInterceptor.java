package com.jme.common.network;

import com.jme.common.util.RxBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by XuJun on 2018/11/7.
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            String[] cookies = originalResponse.header("Set-Cookie").split(";");
            if (null != cookies && cookies.length > 0)
                RxBus.getInstance().post("cookie", cookies[0]);
        }

        return originalResponse;
    }
}
