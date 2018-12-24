package com.jme.lsgoldtrade.config;

/**
 * Created by XuJun on 2018/11/8.
 */
public class Constants {

    private Constants() {

    }

    // Http常量配置
    public static final class HttpConst {

        public static final int Test_WAN = 0; // 测试环境
        public static final int Produce = 1;  // 正式环境

        public static int Envi = Test_WAN;

        private HttpConst() {

        }

        public static String URL_BASE;

        static {
            if (Envi == Test_WAN) {
                URL_BASE = "https://222.190.245.2:18012";
            } else if (Envi == Produce) {
                URL_BASE = "";
            }
        }

    }

    // RxBus常量配置
    public static final class RxBusConst {

        private RxBusConst() {

        }

        public static final String LOGOUT = "logout";
    }

    public static final class Msg {

        private Msg() {

        }

        public static final int MSG_MAINPAGE_UPDATE_MARKET = 0;
        public static final int MSG_MARKET_UPDATE = 1;
        public static final int MSG_UPDATE_DATA = 2;
        public static final int MSG_RELOAD_DATA = 3;
        public static final int MSG_UPDATE_DATA_LANDSCAPE = 4;
        public static final int MSG_RELOAD_DATA_LANDSCAPE = 5;
    }

    public static final class ARouterUriConst {
        public static final String MAIN = "/AccountLoginActivity/MainActivity";
        public static final String RFINEXWEBVIEW = "/JMEBase/JMEBaseWebviewActivity";
        public static final String ACCOUNTLOGIN = "/PersonalFragment/AccountLoginActivity";
        public static final String MOBILELOGIN = "/PersonalFragment/MobileLoginActivity";
        public static final String MESSAGECENTER = "/PersonalFragment/MessageCenterActivity";
        public static final String FEEDBACK = "/PersonalFragment/FeedBackActivity";
        public static final String SETTING = "/PersonalFragment/SettingActivity";
        public static final String MARKETREFRESH = "/SettingActivity/MarketRefreshActivity";
        public static final String SPLASH = "/SettingActivity/SplashActivity";
        public static final String ABOUT = "/SettingActivity/AboutActivity";
        public static final String MARKETDETAIL = "/MarketFragment/MarketDetailActivity";
        public static final String MARKETDETAILLANDSCAPE = "/MarketDetailActivity/MarketDetailLandscapeActivity";
        public static final String CAPITALTRANSFER = "/FragmentHoldPositionBinding/CapitalTransferActivity";
        public static final String DAILYSTATEMENT = "/QueryFragment/DailyStatementActivity";
        public static final String CURRENTHOLDPOSITION = "/QueryFragment/CurrentHoldPositionActivity";
        public static final String HISTORYHOLDPOSITION = "/QueryFragment/HistoryHoldPositionActivity";
        public static final String CURRENTENTRUST = "/QueryFragment/CurrentEntrustActivity";
        public static final String HISTORYENTRUST = "/QueryFragment/HistoryEntrustActivity";
        public static final String CURRENTDEAL = "/QueryFragment/CurrentDealActivity";
        public static final String HISTORYDEAL = "/QueryFragment/HistoryDealActivity";
        public static final String NEWSACTIVITY = "/JME/NewsActivity";
    }

    public static final class Values {
        public static long Second = 1000;
        public static long Minute = 60 * Second;

        public static long TimeInterval_WiFi = 2 * Second;
        public static long TimeInterval_NetWork = 5 * Second;
        public static long TimeInterval_Normal = 3 * Second;

        public static long Multiplier = 1000;
    }

    public static final long SPLASH_DELAY_MILLIS = 1000;

    public static final class IntentConst {

        private IntentConst() {

        }

        public static final int CODE_REQUEST_LANDSCAPE = 10000;
    }

}
