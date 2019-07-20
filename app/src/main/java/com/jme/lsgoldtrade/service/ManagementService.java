package com.jme.lsgoldtrade.service;

import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.AdvertisementVo;
import com.jme.lsgoldtrade.domain.QuestionVo;
import com.jme.lsgoldtrade.domain.BannerVo;
import com.jme.lsgoldtrade.domain.ChannelVo;
import com.jme.lsgoldtrade.domain.CustomerServiceVo;
import com.jme.lsgoldtrade.domain.FenXiShiListVo;
import com.jme.lsgoldtrade.domain.FenXiShiVo;
import com.jme.lsgoldtrade.domain.HistoryBoxVo;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.domain.IsSubscribeVo;
import com.jme.lsgoldtrade.domain.MyOrderVo;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.domain.QuestListTypeVo;
import com.jme.lsgoldtrade.domain.QuestionGuessVo;
import com.jme.lsgoldtrade.domain.SaveNavigatorVo;
import com.jme.lsgoldtrade.domain.StrategyVo;
import com.jme.lsgoldtrade.domain.SubscribeVo;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;
import com.jme.lsgoldtrade.domain.TradingBoxInfoVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.domain.UpdateInfoVo;

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

    public API saveNavigatorList = new API<SaveNavigatorVo>("SaveNavigatorList") {
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

    public API tradingBox = new API<TradingBoxVo>("TradingBox") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.tradingBox(params);
        }
    };

    public API historyBox = new API<List<HistoryBoxVo>>("HistoryBox") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.historyBox(params);
        }
    };

    public API tradingBoxDetails = new API<TradingBoxDetailsVo>("TradingBoxDetails") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.tradingBoxDetails(params);
        }
    };

    public API tradingBoxInfo = new API<TradingBoxInfoVo>("TradingBoxInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.tradingBoxInfo(params);
        }
    };

    public API submitTradingBox = new API<String>("SubmitTradingBox") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.submitTradingBox(params);
        }
    };

    public API getUserAddedServicesStatus = new API<String>("GetUserAddedServicesStatus") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getUserAddedServicesStatus(params);
        }
    };

    public API setSubscribe = new API<SubscribeVo>("Subscribe") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.setSubscribe(params);
        }
    };

    public API fenxishi = new API<List<FenXiShiVo>>("FenXiShiList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.fenxishi(params);
        }
    };

    public API fenxishiList = new API<FenXiShiListVo>("FenXiList") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.fenxishiList(params);
        }
    };

    public API isSubscribe = new API<IsSubscribeVo>("IsSubscribe") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.isSubscribe(params);
        }
    };

    public API myOrder = new API<List<MyOrderVo>>("MyOrder") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.myOrder(params);
        }
    };

    public API revocation = new API<String>("Revocation") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.revocation(params);
        }
    };

    public API orderDetails = new API<MyOrderVo>("OrderDetails") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.orderDetails(params);
        }
    };

    public API rate = new API<String>("Rate") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.rate(params);
        }
    };

    public API hasProfitLossRiskSign = new API<String>("HasProfitLossRiskSign") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.hasProfitLossRiskSign(params);
        }
    };

    public API agree = new API<String>("Agree") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.agree(params);
        }
    };

    public API diyanfangxiang = new API<String>("Agree") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.diyanfangxiang(params);
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

    public API getMaxTradeNum = new API<String>("GetMaxTradeNum") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getMaxTradeNum(params);
        }
    };
}
