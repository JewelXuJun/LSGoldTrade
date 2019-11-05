package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ConditionOrderRunVo;
import com.jme.lsgoldtrade.domain.ConditionPageVo;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class ConditionService extends IService<ConditionApi> {

    public ConditionService() {
        super(Constants.HttpConst.URL_BASE_CONDOTION, true);
    }

    public static ConditionService getInstance() {
        return (ConditionService) getInstance(ConditionService.class);
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

    public API entrustConditionOrder = new API<String>("EntrustConditionOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.entrustConditionOrder(params);
        }
    };

    public API queryConditionOrderById = new API<ConditionOrderInfoVo>("QueryConditionOrderById") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.queryConditionOrderById(params);
        }
    };

    public API queryConditionOrderPage = new API<ConditionPageVo>("QueryConditionOrderPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.queryConditionOrderPage(params);
        }
    };

    public API queryConditionOrderRun = new API<ConditionOrderRunVo>("QueryConditionOrderRun") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.queryConditionOrderRun(params);
        }
    };

    public API querySetStopOrder = new API<ConditionOrderInfoVo>("QuerySetStopOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.querySetStopOrder(params);
        }
    };

    public API revokeConditionOrder = new API<String>("RevokeConditionOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.revokeConditionOrder(params);
        }
    };

    public API updateConditionOrder = new API<String>("UpdateConditionOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.updateConditionOrder(params);
        }
    };

}