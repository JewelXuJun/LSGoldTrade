package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.AccountDetailVo;
import com.jme.lsgoldtrade.domain.AdvertisementVo;
import com.jme.lsgoldtrade.domain.OnlineTimeVo;
import com.jme.lsgoldtrade.domain.OrderVo;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.domain.PasswordStatusVo;
import com.jme.lsgoldtrade.domain.PayIconVo;
import com.jme.lsgoldtrade.domain.ProtocolVo;
import com.jme.lsgoldtrade.domain.QuestionVo;
import com.jme.lsgoldtrade.domain.BannerVo;
import com.jme.lsgoldtrade.domain.ChannelVo;
import com.jme.lsgoldtrade.domain.CustomerServiceVo;
import com.jme.lsgoldtrade.domain.MarketJudgmentListVo;
import com.jme.lsgoldtrade.domain.AnalystVo;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.TradingBoxOrderVo;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.domain.QuestListTypeVo;
import com.jme.lsgoldtrade.domain.QuestionGuessVo;
import com.jme.lsgoldtrade.domain.StrategyVo;
import com.jme.lsgoldtrade.domain.SubscribeVo;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;
import com.jme.lsgoldtrade.domain.TradingBoxInfoVo;
import com.jme.lsgoldtrade.domain.TradingBoxResponse;
import com.jme.lsgoldtrade.domain.UpdateInfoVo;

import java.util.ArrayList;
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

    public API firstThree = new API<List<AdvertisementVo>>("FirstThree") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.firstThree(params);
        }
    };

    public API navigatorList = new API<NavigatorVo>("NavigatorList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.navigatorList(params);
        }
    };

    public API saveNavigatorList = new API<String>("SaveNavigatorList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.saveNavigatorList(params);
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

    public API getVersionInfo = new API<UpdateInfoVo>("GetVersionInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {

            return mApi.getVersionInfo(params);
        }
    };

    public API strategy = new API<List<StrategyVo>>("Strategy") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.strategy(params);
        }
    };

    public API placeOrder = new API<String>("PlaceOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.placeOrder(params);
        }
    };

    public API analystList = new API<List<AnalystVo>>("AnalystList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.analystList(params);
        }
    };

    public API marketJudgeList = new API<MarketJudgmentListVo>("MarketJudgeList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.marketJudgeList(params);
        }
    };

    public API getOrderList = new API<List<TradingBoxOrderVo>>("GetOrderList") {
        @Override
        public Call<TradingBoxResponse> request(HashMap<String, String> params) {
            return mApi.getOrderList(params);
        }
    };

    public API revocation = new API<String>("Revocation") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.revocation(params);
        }
    };

    public API getDetailInfo = new API<TradingBoxOrderVo>("GetDetailInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getDetailInfo(params);
        }
    };

    public API add = new API<String>("Add") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.add(params);
        }
    };

    public API hasProfitLossRiskSign = new API<String>("HasProfitLossRiskSign") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.hasProfitLossRiskSign(params);
        }
    };

    public API getGreeting = new API<QuestionGuessVo>("GetGreeting") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getGreeting(params);
        }
    };

    public API questTypeList = new API<List<QuestListTypeVo>>("QuestTypeList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.questTypeList(params);
        }
    };

    public API answerList = new API<List<CustomerServiceVo>>("AnswerList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.answerList(params);
        }
    };

    public API getQuestionListByType = new API<List<QuestionVo>>("GetQuestionListByType") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getQuestionListByType(params);
        }
    };

    public API openValueAddedServices = new API<String>("OpenValueAddedServices") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.openValueAddedServices(params);
        }
    };

    public API checkOrder = new API<OrderVo>("CheckOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.checkOrder(params);
        }
    };

    public API timeLineList = new API<String>("TimeLineList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.timeLineList(params);
        }
    };

    public API timeLineSave = new API<String>("TimeLineSave") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.timeLineSave(params);
        }
    };

    public API getProtocolVersion = new API<List<ProtocolVo>>("GetProtocolVersion") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getProtocolVersion(params);
        }
    };

    public API insertRatifyAccord = new API<String>("InsertRatifyAccord") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            String token = params.get("token");
            String[] values = params.get("value").split(",");

            List<String> protocolVersionList = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                protocolVersionList.add(values[i]);
            }

            HashMap<String, Object> paramsValue = new HashMap<>();
            paramsValue.put("token", token);
            paramsValue.put("protocolVersionList",protocolVersionList);

            return mApi.insertRatifyAccord(paramsValue);
        }
    };

    public API getListExt = new API<SubscribeStateVo>("GetListExt") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getListExt(params);
        }
    };

    public API setAppSubscribe = new API<SubscribeVo>("SetAppSubscribe") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.setAppSubscribe(params);
        }
    };

    public API getBoxInfo = new API<TradingBoxInfoVo>("GetBoxInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getBoxInfo(params);
        }
    };

    public API queryTradeBoxList = new API<List<TradingBoxVo>>("QueryTradeBoxList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.queryTradeBoxList(params);
        }
    };

    public API queryTradeBoxLossHistoryInfo = new API<TradingBoxHistoryVo>("QueryTradeBoxLossHistoryInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.queryTradeBoxLossHistoryInfo(params);
        }
    };

    public API getTradeBoxByTradeId = new API<TradingBoxDetailsVo>("TradeBoxByTradeId") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getTradeBoxByTradeId(params);
        }
    };

    public API querySubscriberCount = new API<Integer>("QuerySubscriberCount") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.querySubscriberCount(params);
        }
    };

    public API accountDetailList = new API<AccountDetailVo>("AccountDetailList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.accountDetailList(params);
        }
    };

    public API getOnlineTimeList = new API<List<OnlineTimeVo>>("GetOnlineTimeList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getOnlineTimeList(params);
        }
    };

    public API setUserOnlineTime = new API<String>("SetUserOnlineTime") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.setUserOnlineTime(params);
        }
    };

    public API getUserOnlineTime = new API<OnlineTimeVo>("GetUserOnlineTime") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getUserOnlineTime(params);
        }
    };

    public API getUserPasswordSettingInfo = new API<PasswordInfoVo>("GetUserPasswordSettingInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getUserPasswordSettingInfo(params);
        }
    };

    public API hasSettingGesture = new API<String>("HasSettingGesture") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.hasSettingGesture(params);
        }
    };

    public API updatePasswordOpenStatus = new API<String>("UpdatePasswordOpenStatus") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.updatePasswordOpenStatus(params);
        }
    };

    public API setTradePassword = new API<PasswordStatusVo>("SetTradePassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.setTradePassword(params);
        }
    };

    public API sendMessage = new API<String>("SendMessage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.sendMessage(params);
        }
    };

    public API validateLoginPassword = new API<String>("ValidateLoginPassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.validateLoginPassword(params);
        }
    };

    public API unlockTradePassword = new API<String>("UnlockTradePassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.unlockTradePassword(params);
        }
    };

    public API getPayIcon = new API<List<PayIconVo>>("GetPayIcon") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getPayIcon(params);
        }
    };

    public API getCustomerArrearage = new API<String>("GetCustomerArrearage") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getCustomerArrearage(params);
        }
    };

    public API checkValueAddedServicesForClose = new API<String>("CheckValueAddedServicesForClose") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.checkValueAddedServicesForClose(params);
        }
    };

}
