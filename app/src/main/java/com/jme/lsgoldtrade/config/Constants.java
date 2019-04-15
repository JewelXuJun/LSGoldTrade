package com.jme.lsgoldtrade.config;

/**
 * Created by XuJun on 2018/11/8.
 */
public class Constants {

    private Constants() {

    }

    // Http常量配置
    public static final class HttpConst {

        public static final int Test_WAN = 0;
        public static final int Test_LAN = 1;
        public static final int Produce = 2;

        public static int Envi = Test_WAN;

        private HttpConst() {

        }

        public static String URL_BASE_MARKET;
        public static String URL_BASE_TRADE;
        public static String URL_BASE_MANAGEMENT;
        public static String URL_OPEN_ACCOUNT = "https://portal.taijs.com/lsjhj-openacct/wap/register.html";
        public static String URL_INFO;

        static {
            if (Envi == Test_WAN) {
                URL_BASE_MARKET = "https://222.190.245.2:18012";
                URL_BASE_TRADE = "https://222.190.245.2:18012";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/ios/cmsComtentTxt/get?contentId=";
            } else if (Envi == Test_LAN) {
                URL_BASE_MARKET = "http://192.168.10.171:8080";
                URL_BASE_TRADE = "http://192.168.10.171:8081";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/ios/cmsComtentTxt/get?contentId=";
            } else if (Envi == Produce) {
                URL_BASE_MARKET = "";
                URL_BASE_TRADE = "";
                URL_BASE_MANAGEMENT = "";
                URL_INFO = "";
            }
        }

    }

    // RxBus常量配置
    public static final class RxBusConst {

        private RxBusConst() {

        }

        public static final String RXBUS_MAINPAGE_REFRESH = "MainPageRefresh";
        public static final String RXBUS_TRADEFRAGMENT = "TradeFragment";
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
    }

    public static final long SPLASH_DELAY_MILLIS = 2000;

    public static final class IntentConst {

        private IntentConst() {

        }

        public static final int CODE_REQUEST_LANDSCAPE = 10000;
    }

}
