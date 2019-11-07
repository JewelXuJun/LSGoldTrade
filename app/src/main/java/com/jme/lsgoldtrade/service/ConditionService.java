package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ConditionOrderRunVo;
import com.jme.lsgoldtrade.domain.ConditionPageVo;
import com.jme.lsgoldtrade.domain.ConditionSheetResponse;
import com.jme.lsgoldtrade.domain.QuerySetStopOrderResponse;

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
            HashMap<String, Object> paramsValue = new HashMap<>();
            paramsValue.put("accountId", Long.parseLong(params.get("accountId")));
            paramsValue.put("bsFlag", Integer.parseInt(params.get("bsFlag")));
            paramsValue.put("contractId", params.get("contractId"));
            paramsValue.put("effectiveTimeFlag", Integer.parseInt(params.get("effectiveTimeFlag")));
            paramsValue.put("entrustNumber", Integer.parseInt(params.get("entrustNumber")));
            paramsValue.put("ocFlag", Integer.parseInt(params.get("ocFlag")));
            paramsValue.put("tradingType", Integer.parseInt(params.get("tradingType")));
            paramsValue.put("type", Integer.parseInt(params.get("type")));
            if (params.containsKey("triggerPrice"))
                paramsValue.put("triggerPrice", Long.parseLong(params.get("triggerPrice")));
            if (params.containsKey("stopProfitPrice"))
                paramsValue.put("stopProfitPrice", Long.parseLong(params.get("stopProfitPrice")));
            if (params.containsKey("stopLossPrice"))
                paramsValue.put("stopLossPrice", Long.parseLong(params.get("stopLossPrice")));

            return mApi.entrustConditionOrder(paramsValue);
        }
    };

    public API queryConditionOrderById = new API<ConditionOrderInfoVo>("QueryConditionOrderById") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            HashMap<String, Object> paramsValue = new HashMap<>();
            paramsValue.put("id", Long.parseLong(params.get("id")));

            return mApi.queryConditionOrderById(paramsValue);
        }
    };

    public API queryConditionOrderPage = new API<ConditionPageVo>("QueryConditionOrderPage") {
        @Override
        public Call<ConditionSheetResponse> request(HashMap<String, String> params) {

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
        public Call<QuerySetStopOrderResponse> request(HashMap<String, String> params) {

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
            HashMap<String, Object> paramsValue = new HashMap<>();
            paramsValue.put("id", Long.parseLong(params.get("id")));
            paramsValue.put("effectiveTimeFlag", Integer.parseInt(params.get("effectiveTimeFlag")));
            paramsValue.put("entrustNumber", Integer.parseInt(params.get("entrustNumber")));
            if (params.containsKey("triggerPrice"))
                paramsValue.put("triggerPrice", Long.parseLong(params.get("triggerPrice")));
            if (params.containsKey("stopProfitPrice"))
                paramsValue.put("stopProfitPrice", Long.parseLong(params.get("stopProfitPrice")));
            if (params.containsKey("stopLossPrice"))
                paramsValue.put("stopLossPrice", Long.parseLong(params.get("stopLossPrice")));

            return mApi.updateConditionOrder(paramsValue);
        }
    };

}
