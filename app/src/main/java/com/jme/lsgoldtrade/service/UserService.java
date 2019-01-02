package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.domain.NoticePageVo;
import com.jme.lsgoldtrade.domain.User;
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

    public API kaptcha = new API<ImageVerifyCodeVo>("Kaptcha") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.kaptcha();
        }
    };

    public API loginMsg = new API<String>("LoginMsg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.loginMsg(params);
        }
    };

    public API login = new API<UserInfoVo>("Login") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.login(params);
        }
    };

    public API logout = new API<String>("Logout") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.logout(params);
        }
    };

    public API changeloginpassword = new API<String>("Changeloginpassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.changeloginpassword(params);
        }
    };

    public API fundInoutMsg = new API<String>("FundInoutMsg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.fundInoutMsg();
        }
    };

    public API noticepage = new API<NoticePageVo>("NoticePage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.noticepage(params);
        }
    };

    public API dealpage = new API<DealPageVo>("DealPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dealpage(params);
        }
    };

}
