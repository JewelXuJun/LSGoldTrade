package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.domain.SignMessageVo;
import com.jme.lsgoldtrade.domain.SignVo;

import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class WithholdAccountService extends IService<WithholdAccountApi> {

    public WithholdAccountService() {
        super(Constants.HttpConst.URL_BASE_WITHHOLD_ACCOUNT, true);
    }

    public static WithholdAccountService getInstance() {
        return (WithholdAccountService) getInstance(WithholdAccountService.class);
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

    public API getBanks = new API<List<BankVo>>("GetBanks") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getBanks(params);
        }
    };

    public API sendSignMessage = new API<SignMessageVo>("SendSignMessage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.sendSignMessage(params);
        }
    };

    public API sign = new API<SignVo>("Sign") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.sign(params);
        }
    };

    public API getCustomerSignBankList = new API<List<BankVo>>("GetCustomerSignBankList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getCustomerSignBankList(params);
        }
    };
}
