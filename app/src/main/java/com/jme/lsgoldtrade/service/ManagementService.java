package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.BannerVo;
import com.jme.lsgoldtrade.domain.ChannelVo;
import com.jme.lsgoldtrade.domain.InfoVo;

import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class ManagementService extends IService<ManagementApi> {

    public ManagementService() {
        super(Constants.HttpConst.URL_BASE_MANAGEMENT, true);
    }

    public static ManagementService getInstance() {
        return (ManagementService) getInstance(ManagementService.class);
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

    public API feedback = new API<String>("FeedBack") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.feedback(params);
        }
    };

    public API bannerAllList = new API<List<BannerVo>>("BannerAllList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.bannerAllList(params);
        }
    };

    public API study = new API<InfoVo>("Study") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.study(params);
        }
    };

    public API channelAllList = new API<List<ChannelVo>>("ChannelAllList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.channelAllList(params);
        }
    };

    public API channelList = new API<InfoVo>("ChannelList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.channelList(params);
        }
    };
}
