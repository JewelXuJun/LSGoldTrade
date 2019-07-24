package com.jme.lsgoldtrade.config;

/**
 * 网络配置
 */
public class Constants {

    private Constants() {

    }

    // Http常量配置
    public static final class HttpConst {

        public static final int TEST_WAN = 0;
        public static final int TEST_LAN = 1;
        public static final int PRODUCE = 2;
        public static final int UAT = 3;

        public static int Envi = UAT;

        private HttpConst() {

        }

        public static String URL_BASE_MARKET;         // 行情
        public static String URL_BASE_TRADE;          // 交易
        public static String URL_BASE_ACCOUNT;        // 增值服务
        public static String URL_BASE_MANAGEMENT;     // 管理后台 banner 资讯等
        public static String URL_INFO;                // 资讯详情
        public static String URL_REGISTER_AGGREMENT = "http://www.taijs.com/upload/yhxy.htm";
        public static String URL_DISCLAIMER = "http://www.taijs.com/upload/mianze.html";
        public static String URL_PRIVACY_POLICY = "http://www.taijs.com/upload/yszc.htm";
        public static String URL_OPEN_ACCOUNT_COURSE = "http://www.taijs.com/upload/glht/khjc.html";
        public static String URL_TRADE_RULE = "http://www.taijs.com/upload/jygz/jygz.html";
        public static String URL_TRADE_BOX_INTRODUCTION = "http://www.taijs.com/upload/jyxz/jyxz.html";
        public static String URL_TRADE_BOX_QUESTION = "http://www.taijs.com/upload/jyxz/cjwt.html";


        static {
            if (Envi == TEST_WAN) {
                URL_BASE_MARKET = "https://222.190.245.2:18012";
                URL_BASE_TRADE = "https://222.190.245.2:18012";
                URL_BASE_ACCOUNT = "http://tjs418008.developer.jsdttec.com";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
            } else if (Envi == TEST_LAN) {
                URL_BASE_MARKET = "http://192.168.10.171:8080";
                URL_BASE_TRADE = "http://192.168.10.171:8081";
                URL_BASE_ACCOUNT = "http://192.168.10.41:8008";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
            } else if (Envi == PRODUCE) {
                URL_BASE_MARKET = "https://180.96.49.165:18002";
                URL_BASE_TRADE = "https://180.96.49.165:18001";
                URL_BASE_ACCOUNT = "https://180.96.49.165:18001";
                URL_BASE_MANAGEMENT = "https://u.taijs.com";
                URL_INFO = "https://u.taijs.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
            } else if (Envi == UAT) {
                URL_BASE_MARKET = "http://tjsuat1866543.developer.jsdttec.com";
                URL_BASE_TRADE = "http://tjsuat1866666.developer.jsdttec.com";
                URL_BASE_ACCOUNT = "http://192.168.10.189:8008";
                URL_BASE_MANAGEMENT = "http://tjsuat.developer.jsdttec.com";
                URL_INFO = "http://tjsuat.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
            }
        }

    }

    // RxBus常量配置
    public static final class RxBusConst {

        private RxBusConst() {

        }

        public static final String RXBUS_MAINPAGE_REFRESH = "MainPageRefresh";
        public static final String RXBUS_TRADE = "Trade";
        public static final String RXBUS_TRADEFRAGMENT_HOLD = "TradeFragmentHold";
        public static final String RXBUS_DECLARATIONFORM_UPDATE = "DeclarationFormUpdate";
        public static final String RXBUS_DECLARATIONFORM_HOLDPOSITION_SELECT = "DeclarationFormHoldPositionSelect";
        public static final String RXBUS_DECLARATIONFORM_HOLDPOSITION_UNSELECT = "DeclarationFormHoldPositionUnSelect";
        public static final String RXBUS_DECLARATIONFORM_SHOW = "DeclarationFormShow";
        public static final String RXBUS_DECLARATIONFORM_CANCEL = "DeclarationFormCancel";
        public static final String RXBUS_DECLARATIONFORM_CONFIRM = "DeclarationFormConfirm";
        public static final String RXBUS_MARKETDETAIL_QUICK = "MarketDetailQuick";
        public static final String RXBUS_SYNTIME = "SynTimeMessage";
        public static final String RXBUS_CANCEL = "cancel";
        public static final String RXBUS_CANCEL_MAIN = "cancelMain";
        public static final String RXBUS_ORDER_SUCCESS = "orderSuccess";
        public static final String RXBUS_CHEDAN = "chedan";
        public static final String RXBUS_CHEDAN_FRAGMENT = "chedanfragment";
        public static final String RXBUS_CHICANG_FRAGMENT = "chicangfragment";
        public static final String RXBUS_CUSTOMER_SERVICE = "customerservice";
        public static final String RXBUS_BIND_SUCCESS = "BindSuccess";
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
        public static final int MSG_TRADE_UPDATE_DATA = 6;
        public static final int MSG_POSITION_UPDATE_DATA = 7;
        public static final int MSG_DECLARATIONFORM_POSITION_UPDATE_DATA = 8;
        public static final int MSG_TRADE_POSITION_UPDATE_DATA = 9;
        public static final int MSG_SYNTIME = 10;
        public static final int MSG_POSITION_UPDATE_ACCOUNT_DATA = 11;
        public static final int MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA = 12;
        public static final int MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA = 13;
        public static final int MSG_BING_ACCOUNT_SUCCESS = 14;

    }

    public static final class ARouterUriConst {
        public static final String MAIN = "/JME/MainActivity";
        public static final String JMEWEBVIEW = "/JMEBase/JMEBaseWebViewActivity";
        public static final String ACCOUNTLOGIN = "/PersonalFragment/AccountLoginActivity";
        public static final String MOBILELOGIN = "/PersonalFragment/MobileLoginActivity";
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
        public static final String CURRENTENTRUST = "/QueryFragment/CurrentEntrustActivity";
        public static final String HISTORYENTRUST = "/QueryFragment/HistoryEntrustActivity";
        public static final String CURRENTDEAL = "/QueryFragment/CurrentDealActivity";
        public static final String HISTORYDEAL = "/QueryFragment/HistoryDealActivity";
        public static final String NEWSCENTERACTIVITY = "/JME/NewsCenterActivity";
        public static final String NEWSDETAILACTIVITY = "/NewsCenterActivity/NewsDetailActivity";
        public static final String BEGINNERSACTIVITY = "/MainPageFragment/BeginnersActivity";
        public static final String GUARANTEEFUNDSETTINGACTIVITY = "/HoldPositionFragment/GuaranteeFundSettingActivity";
        public static final String TRADINGBOX = "/MainPageFragment/TradingBoxActivity";
        public static final String TRADINGBOXDETAIL = "/TradingboxDetails/TradingBoxDetailActivity";
        public static final String RELEVANTINFO = "/TradingboxDetails/RelevantInfoActivity";
        public static final String PLACEORDER = "/TradingboxDetails/PlaceOrderActivity";
        public static final String TRADINGBOXHISTROY = "/TradingBoxActivity/TradingBoxHistroyActivity";
        public static final String TRADINGBOXORDER = "/TradingBoxActivity/TradingBoxOrderActivity";
        public static final String ORDERDETAILS = "/Order/OrderDetailsActivity";
        public static final String FASTENTRY = "/Mainpager/FastEntryActivity";
        public static final String DIYAN = "/Mainpager/DiYanActivity";
        public static final String VALUEADDEDSERVICE = "/Personal/ValueAddedServiceActivity";
        public static final String YANPAN = "/Mainpager/HangQingYanPanActivity";
        public static final String YUJING = "/Market/YujingActivity";
        public static final String ECONOMICCALENDAR = "/Mainpager/EconomicCalendarActivity";
        public static final String FORGETPASSWORD = "/Login/ForgetPasswordActivity";
        public static final String REGISTER = "/Login/RegisterActivity";
        public static final String CUSTOMSERVICE = "/Personal/CustomerServiceActivity";
        public static final String QUESTIONABOUT = "/Personal/QuestionAboutActivity";
        public static final String REGISTERSUCCESS = "/PersonalFragment/RegisterSuccessActivity";
        public static final String MYSUBSCRIBE = "/PersonalFragment/MySubscribeActivity";
        public static final String BINDACCOUNT = "/Login/BindAccountActivity";
        public static final String SETLOGINPASSWORD = "/Login/SetLoginPasswordActivity";
        public static final String SETLOGINPASSWORDSUCCESS = "/SetLoginPasswordActivity/SetLoginPasswordSuccessActivity";
        public static final String AUTHENTICATION = "/Login/AuthenticationActivity";
        public static final String VALUESERVICESUCCESS = "/Personal/ValueServiceSuccessActivity";
        public static final String RECHARGE = "/Personal/RechargeActivity";
        public static final String CHECKSERVICE = "/Personal/CheckServiceActivity";
        public static final String CASH = "/Personal/CashActivity";
        public static final String THAW = "/Personal/ThawActivity";
        public static final String APPLICATIONFORCASH = "/Personal/ApplicationForCashActivity";
        public static final String DETAILS = "/Personal/DetailsActivity";
    }

    public static final long SPLASH_DELAY_MILLIS = 2000;

    public static final class IntentConst {

        private IntentConst() {

        }

        public static final int CODE_REQUEST_LANDSCAPE = 10000;
    }

}
