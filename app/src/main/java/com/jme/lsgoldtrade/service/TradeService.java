package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.BindAccountResponse;
import com.jme.lsgoldtrade.domain.BindAccountVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.DailyStatementVo;
import com.jme.lsgoldtrade.domain.DealHistoryPageVo;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.OrderHisPageVo;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.domain.WarnVo;
import java.util.HashMap;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class TradeService extends IService<TradeApi> {

    public TradeService() {
        super(Constants.HttpConst.URL_BASE_TRADE, true);
    }

    public static TradeService getInstance() {
        return (TradeService) getInstance(TradeService.class);
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

    public API account = new API<AccountVo>("Account") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.account(params);
        }
    };

    public API inoutmoney = new API<String>("InoutMoney") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.inoutmoney(params);
        }
    };

    public API transpage = new API<InOutTurnOverVo>("Transpage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.transpage(params);
        }
    };

    public API dailystatement = new API<DailyStatementVo>("DailyStatement") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dailystatement(params);
        }
    };

    public API orderpage = new API<OrderPageVo>("OrderPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.orderpage(params);
        }
    };

    public API orderhispage = new API<OrderHisPageVo>("OrderHisPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.orderhispage(params);
        }
    };

    public API dealpage = new API<DealPageVo>("DealPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dealpage(params);
        }
    };

    public API dealhispage = new API<DealHistoryPageVo>("DealHisPage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.dealhispage(params);
        }
    };

    public API position = new API<PositionPageVo>("Position") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.position(params);
        }
    };

    public API contractInfo = new API<List<ContractInfoVo>>("ContractInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.contractInfo(params);
        }
    };

    public API limitOrder = new API<String>("LimitOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.limitOrder(params);
        }
    };

    public API marketOrder = new API<String>("MarketOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.marketOrder(params);
        }
    };

    public API revocateorder = new API<String>("RevocateOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.revocateorder(params);
        }
    };

    public API minReserveFund = new API<String>("MinReserveFund") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.minReserveFund(params);
        }
    };

    public API setWarn = new API<String>("SetWarn") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.setWarn(params);
        }
    };

    public API warnInfo = new API<WarnVo>("WarnInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.warnInfo(params);
        }
    };

    public API icbcMsg = new API<String>("IcbcMsg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.icbcMsg(params);
        }
    };

    public API registerMsg = new API<String>("RegisterMsg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.registerMsg(params);
        }
    };

    public API verifyIdCard = new API<String>("VerifyIdCard") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.verifyIdCard(params);
        }
    };

    public API registerLogin = new API<UserInfoVo>("RegisterLogin") {
        @Override
        public Call<LoginResponse> request(HashMap<String, String> params) {

            return mApi.registerLogin(params);
        }
    };

    public API resetLoginPasswordMsg = new API<String>("ResetLoginPasswordMsg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.resetLoginPasswordMsg(params);
        }
    };

    public API resetLoginPassword = new API<String>("ResetLoginPassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.resetLoginPassword(params);
        }
    };

    public API bindAccount = new API<BindAccountVo>("BindAccount") {
        @Override
        public Call<BindAccountResponse> request(HashMap<String, String> params) {

            return mApi.bindAccount(params);
        }
    };

    public API setLoginPassword = new API<String>("SetLoginPassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.setLoginPassword(params);
        }
    };

    public API whetherIdCard = new API<IdentityInfoVo>("WhetherIdCard") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.whetherIdCard(params);
        }
    };

}
