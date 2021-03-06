package com.jme.lsgoldtrade.config;

public class Constants {

    private Constants() {

    }

    public static final class HttpConst {

        public static final int TEST_WAN = 0;
        public static final int TEST_LAN = 1;
        public static final int PRODUCE = 2;
        public static final int UAT = 3;
        public static final int PREPRODUCE = 4;
        public static final int SECURITY = 5;
        public static final int SECURITY_WAN = 6;

        public static int Envi = PRODUCE;

        private HttpConst() {

        }

        public static String URL_BASE_MARKET;                   // 行情
        public static String URL_BASE_TRADE;                    // 交易
        public static String URL_BASE_ACCOUNT;                  // 增值服务
        public static String URL_BASE_WITHHOLD_ACCOUNT;         // 签约代扣
        public static String URL_BASE_CONDOTION;                // 条件单
        public static String URL_BASE_MANAGEMENT;               // 管理后台 banner 资讯等
        public static String URL_INFO;                          // 资讯详情
        public static String URL_METAL_PAY;
        public static String URL_REGISTER_AGGREMENT = "http://www.taijs.com/upload/yhxy.htm";
        public static String URL_DISCLAIMER = "http://www.taijs.com/upload/mianze.html";
        public static String URL_PRIVACY_POLICY = "http://www.taijs.com/upload/yszc.htm";
        public static String URL_OPEN_ACCOUNT_COURSE = "http://www.taijs.com/upload/glht/khjc.html";
        public static String URL_TRADE_RULE = "http://www.taijs.com/upload/jygz/jygz.html";
        public static String URL_TRADE_BOX_INTRODUCTION = "http://www.taijs.com/upload/jyxz/jyxz.html";
        public static String URL_TRADE_BOX_QUESTION = "http://www.taijs.com/upload/jyxz/cjwt.html";
        public static String URL_TRADING_BOX_AGREEMENT = "http://www.taijs.com/upload/jyxzfwxy.htm";
        public static String URL_ENTRUST_RISK_AGREEMENT = "http://www.taijs.com/upload/wtfkfwxy.htm";
        public static String URL_ECONOMIC_CALENDAR = "https://rili-d.jin10.com/open.php?fontSize=14px&theme=primary";
        public static String URL_NEWS = "https://www.jin10.com/example/jin10.com.html?fontSize=14px&theme=white";
        public static String URL_MORE_ICON = "https://tjshj.oss-cn-beijing.aliyuncs.com/prod/syscofig/app/gengduo.png";
        public static String URL_CONDITION_SHEET_EXPLAIN = "http://www.taijs.com/upload/tjdsysm.html";
        public static String URL_AGREEMENT_HUAXING = "https://www.taijs.com/upload/hxyhdkxy.htm";
        public static String URL_TRADINGBOX;
        public static String URL_TRADINGBOXINFO;

        static {
            if (Envi == TEST_WAN) {
                URL_BASE_MARKET = "https://222.190.245.2:18012";
                URL_BASE_TRADE = "https://222.190.245.2:18012";
                URL_BASE_ACCOUNT = "http://tjs418008.developer.jsdttec.com";
                URL_BASE_WITHHOLD_ACCOUNT = "http://tjs418008.developer.jsdttec.com";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://uuat.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://192.168.10.171:18080/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://192.168.10.171:18080/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == TEST_LAN) {
                URL_BASE_MARKET = "http://192.168.10.171:8080";
                URL_BASE_TRADE = "http://192.168.10.171:8081";
                URL_BASE_ACCOUNT = "http://192.168.10.41:8008";
                URL_BASE_WITHHOLD_ACCOUNT = "http://192.168.10.41:8008";
                URL_BASE_MANAGEMENT = "http://tjs.developer.jsdttec.com";
                URL_INFO = "http://tjs.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://uuat.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://192.168.10.171:18080/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://192.168.10.171:18080/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == PRODUCE) {
                URL_BASE_MARKET = "https://180.96.49.165:18002";
                URL_BASE_TRADE = "https://180.96.49.165:18001";
                URL_BASE_ACCOUNT = "https://180.96.49.165:18001";
                URL_BASE_WITHHOLD_ACCOUNT = "https://www.taijs.com";
                URL_BASE_CONDOTION = "https://180.96.49.165:18001";
                URL_BASE_MANAGEMENT = "https://www.taijs.com";
                URL_INFO = "https://www.taijs.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "https://goldmall.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "https://www.taijs.com/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "https://www.taijs.com/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == UAT) {
                URL_BASE_MARKET = "http://tjsuat1866543.developer.jsdttec.com";
                URL_BASE_TRADE = "http://tjsuat1866666.developer.jsdttec.com";
                URL_BASE_ACCOUNT = "https://wxtest.jsdttec.com";
                URL_BASE_WITHHOLD_ACCOUNT = "https://wxtest.jsdttec.com";
                URL_BASE_MANAGEMENT = "http://tjsuat.developer.jsdttec.com";
                URL_INFO = "http://tjsuat.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://uuat.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://tjsuat.developer.jsdttec.com/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://tjsuat.developer.jsdttec.com/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == PREPRODUCE) {
                URL_BASE_MARKET = "http://180.97.47.179:18080";
                URL_BASE_TRADE = "http://180.97.47.179:18080";
                URL_BASE_ACCOUNT = "http://180.97.47.179:18080";
                URL_BASE_WITHHOLD_ACCOUNT = "http://180.97.47.179:18080";
                URL_BASE_CONDOTION = "http://180.97.47.179:18080";
                URL_BASE_MANAGEMENT = "http://180.97.47.179:18080";
                URL_INFO = "http://180.97.47.179:18080/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://180.97.47.179:18080/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://180.97.47.179:18080/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://180.97.47.179:18080/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == SECURITY) {
                URL_BASE_MARKET = "http://222.190.245.2:18038";
                URL_BASE_TRADE = "http://222.190.245.2:18038";
                URL_BASE_ACCOUNT = "http://222.190.245.2:18038";
                URL_BASE_WITHHOLD_ACCOUNT = "http://222.190.245.2:18038";
                URL_BASE_MANAGEMENT = "http://222.190.245.2:18038";
                URL_INFO = "http://222.190.245.2:18038/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://uuat.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://222.190.245.2:18038/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://222.190.245.2:18038/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            } else if (Envi == SECURITY_WAN) {
                URL_BASE_MARKET = "http://1786543.developer.jsdttec.com";
                URL_BASE_TRADE = "http://1788081.developer.jsdttec.com";
                URL_BASE_ACCOUNT = "http://1788008.developer.jsdttec.com";
                URL_BASE_WITHHOLD_ACCOUNT = "http://17838008.developer.jsdttec.com";
                URL_BASE_CONDOTION = "http://1778088.developer.jsdttec.com";
                URL_BASE_MANAGEMENT = "http://17818090.developer.jsdttec.com";
                URL_INFO = "http://17818090.developer.jsdttec.com/tjsmanage/infoapi/v1/android/cmsComtentTxt/get?contentId=";
                URL_METAL_PAY = "http://uuat.taijs.com/swhj/navigate/index?token=";
                URL_TRADINGBOX = "http://17818081.developer.jsdttec.com/tjsmanage/openacct/wap/tradingBox.html";
                URL_TRADINGBOXINFO = "http://17818081.developer.jsdttec.com/tjsmanage/openacct/wap/tradingBoxDetail.html?tradeId=";
            }
        }

    }

    public static final class RxBusConst {

        private RxBusConst() {

        }

        public static final String RXBUS_MAINPAGE_REFRESH = "MainPageRefresh";
        public static final String RXBUS_TRANSACTION_PLACE_ORDER = "TransactionPlaceOrder";
        public static final String RXBUS_TRANSACTION_HOLD_POSITIONS = "TransactionHoldPositions";
        public static final String RXBUS_TRANSACTION_CANCEL_ORDER = "TransactionCancelOrder";
        public static final String RXBUS_TRANSACTION_HOLD_POSITIONS_UPDATE = "TransactionHoldPositionsUpdate";
        public static final String RXBUS_MARKETDETAIL_QUICK = "MarketDetailQuick";
        public static final String RXBUS_SYNTIME = "SynTimeMessage";
        public static final String RXBUS_ORDER_SUCCESS = "orderSuccess";
        public static final String RXBUS_CAPITALTRANSFER_SUCCESS = "capitalTransferSuccess";
        public static final String RXBUS_CUSTOMER_SERVICE = "customerservice";
        public static final String RXBUS_BIND_SUCCESS = "bindSuccess";
        public static final String RXBUS_LOGIN_SUCCESS = "loginSuccess";
        public static final String RXBUS_LOGOUT_SUCCESS = "logoutSuccess";
        public static final String RXBUS_FAST_MANAGEMENT_EDIT = "fastManagementEdit";
        public static final String RXBUS_MARKET_UNIT_SORT_SUCCESS = "marketUnitSortSuccess";
        public static final String RXBUS_ELECTRONICCARD_UPDATE = "electronicCardUpdate";
        public static final String RXBUS_ELECTRONICCARD_INOUT_SUCCESS = "electronicCardInoutSuccess";
        public static final String RXBUS_TRADING_PASSWORD_SETTING = "tradingPasswordSetting";
        public static final String RXBUS_TRADING_PASSWORD_SETTING_SUCCESS = "tradingPasswordSettingSuccess";
        public static final String RXBUS_TRADING_PASSWORD_CANCEL = "tradingPasswordCancel";
        public static final String RXBUS_GESTURU_MODIFY_SUCCESS = "gesturuModifySuccess";
        public static final String RXBUS_TRANSACTION_HOLDPOSITIONS_REFRESH = "transactionHoldPositionsRefresh";
        public static final String RXBUS_TRANSACTION_CONDITION_ORDER_RUN = "transactionConditionOrderRun";
        public static final String RXBUS_TRANSACTION_CONDITION_SHEET_MODIFY = "transactionConditionSheetModify";
        public static final String RXBUS_TRANSACTION_CONDITION_OWN = "transactionConditionOwn";
        public static final String RXBUS_TRANSACTION_STOP_SHEET_UPDATE = "transactionStopSheetUpdate";
        public static final String RXBUS_TRANSACTION_STOP_SHEET_CANCEL_ORDER = "transactionStopSheetCancelOrder";
        public static final String RXBUS_TRANSACTION_STOP_SHEET_MODIFY_ORDER = "transactionStopSheetModifyOrder";
        public static final String RXBUS_INCREMENT_ARREARS = "incrementArrears";
        public static final String RXBUS_METAL_PAY = "metalPay";
        public static final String RXBUS_METAL_PAY_SUCCESS = "metalPaySuccess";
        public static final String RXBUS_SELECT_BANK_CARD_SUCCESS = "selectBankCardSuccess";
        public static final String RXBUS_ZJHZ_SETPASSWORD = "zjhzSetpassword";
        public static final String RXBUS_ZJHZ_SETPASSWORD_SUCCESS = "zjhzSetPasswordSuccess";
        public static final String RXBUS_WDDY_SETPASSWORD  = "wddySetPassword";
        public static final String RXBUS_WDDY_SETPASSWORD_SUCCESS  = "wddySetPasswordSuccess";
        public static final String RXBUS_BUY_MORE_SETPASSWORD_SUCCESS  = "buyMoreSetPasswordSuccess";
        public static final String RXBUS_SALE_EMPTY_SETPASSWORD_SUCCESS  = "saleEmptySetPasswordSuccess";
        public static final String RXBUS_DECLARATION_FORM_SETPASSWORD_SUCCESS  = "declarationSetPasswordSuccess";
        public static final String RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD  = "mainPageTrainBoxSetPassword";
        public static final String RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD_SUCCESS  = "mainPageTrainBoxSetPasswordSuccess";
        public static final String RXBUS_CJRL_SETPASSWORD  = "cjrlSetPassword";
        public static final String RXBUS_CJRL_SETPASSWORD_SUCCESS  = "cjrlSetPasswordSuccess";
        public static final String RXBUS_HQYP_SETPASSWORD  = "hqypSetPassword";
        public static final String RXBUS_HQYP_SETPASSWORD_SUCCESS  = "hqypSetPasswordSuccess";
        public static final String RXBUS_PERSON_WDDY_SETPASSWORD_SUCCESS  = "personWddySetPasswordSuccess";
        public static final String RXBUS_HQYP_BUY_MORE = "hqypBuyMore";
        public static final String RXBUS_HQYP_SALE_EMPTY = "hqypSaleEmpty";
        public static final String RXBUS_HQYP_DECLARATION_FORM = "hqypDeclarationForm";
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
        public static final int MSG_TRANSACTION_UPDATE_DATA = 6;
        public static final int MSG_TRADE_POSITION_UPDATE_DATA = 7;
        public static final int MSG_SYNTIME = 8;
        public static final int MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA = 9;
        public static final int MSG_JUDGEMENT_UPDATE_DATA = 10;
        public static final int MSG_JUDGEMENT_RELOAD_DATA = 11;
        public static final int MSG_UARNING_PDATE_DATA = 12;
        public static final int MSG_WXXCX = 13;

    }

    public static final class ARouterUriConst {

        private ARouterUriConst() {

        }

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
        public static final String MARKETEDITSORT = "/MarketDetailActivity/MarketEditSortActivity";
        public static final String CAPITALTRANSFER = "/FragmentHoldPositionBinding/CapitalTransferActivity";
        public static final String ELECTRONICCARDTRANSFER = "/CapitalTransferActivity/ElectronicCardTransferActivity";
        public static final String ELECTRONICCARDINOUTMONEY = "/CapitalTransferActivity/ElectronicCardInOutMoneyActivity";
        public static final String BANKRESERVE = "/CapitalTransferActivity/BankReserveActivity";
        public static final String DAILYSTATEMENT = "/QueryFragment/DailyStatementActivity";
        public static final String NEWSCENTERACTIVITY = "/JME/NewsCenterActivity";
        public static final String NEWSDETAILACTIVITY = "/NewsCenterActivity/NewsDetailActivity";
        public static final String BEGINNERSACTIVITY = "/MainPageFragment/BeginnersActivity";
        public static final String ENTRUSTRISKMANAGEMENT = "/HoldPositionsFragment/EntrustRiskManagementActivity";
        public static final String QUERY = "/HoldPositionsFragment/QueryActivity";
        public static final String TRADINGBOX = "/MainPageFragment/TradingBoxActivity";
        public static final String TRADINGBOXDETAIL = "/TradingboxDetails/TradingBoxDetailActivity";
        public static final String RELEVANTINFO = "/TradingboxDetails/RelevantInfoActivity";
        public static final String PLACEORDER = "/TradingboxDetails/PlaceOrderActivity";
        public static final String TRADINGBOXHISTROY = "/TradingBoxActivity/TradingBoxHistroyActivity";
        public static final String TRADINGBOXORDER = "/TradingBoxActivity/TradingBoxOrderActivity";
        public static final String ORDERDETAILS = "/Order/OrderDetailsActivity";
        public static final String FASTMANAGEMENT = "/Mainpager/FastManagementActivity";
        public static final String DEFERRED = "/Mainpager/DeferredActivity";
        public static final String VALUEADDEDSERVICE = "/Personal/ValueAddedServiceActivity";
        public static final String MARKETJUDGMENT = "/Mainpager/MarketJudgmentActivity";
        public static final String WARNING = "/Market/WarningActivity";
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
        public static final String CHECKUSERINFO = "/Personal/CheckUserInfoActivity";
        public static final String WITHDRAW = "/Personal/WithdrawActivity";
        public static final String BANKCARD = "/CheckServiceActivity/BankCardActivity";
        public static final String WITHHOLD = "/Main/WithholdActivity";
        public static final String WITHHOLDCONTRACT = "/Main/WithholdContractActivity";
        public static final String WITHDRAWRESULT = "/Personal/WithdrawResultActivity";
        public static final String ACCOUNTSECURITY = "/PersonalFragment/AccountSecurityActivity";
        public static final String TRADINGPASSWORDVALIDATE = "/AccountSecurityActivity/TradingPasswordValidateActivity";
        public static final String TRADINGPASSWORDSETTING = "/AccountSecurityActivity/TradingPasswordSettingActivity";
        public static final String VALIDATETRADINGPASSWORD = "/AccountSecurityActivity/ValidateTradingPasswordActivity";
        public static final String TRADINGPASSWORDSETTINGCONFIRM = "/TradingPasswordSettingActivity/TradingPasswordSettingConfirmActivity";
        public static final String FINGERPRINT = "/AccountSecurityActivity/FingerprintActivity";
        public static final String GESTURE = "/AccountSecurityActivity/GestureActivity";
        public static final String GESTURESETTING = "/GestureActivity/GestureSettingActivity";
        public static final String GESTURESETTINGVALIDATE = "/GestureSettingActivity/GestureSettingValidateActivity";
        public static final String ONLINEDURATION = "/AccountSecurityActivity/OnlineDurationActivity";
        public static final String UNLOCKTRADINGPASSWORD = "/TransactionFragment/UnlockTradingPasswordActivity";
        public static final String CONDITIONSHEET = "/TransactionFragment/ConditionSheetActivity";
        public static final String METAL = "/JMEBase/MetalActivity";
        public static final String SELECTBANKCARD = "/Personal/SelectBankCardActivity";
        public static final String OPENINCREMENT = "/Personal/OpenIncrementActivity";
    }

    public static final long SPLASH_DELAY_MILLIS = 2000;

    public static final class IntentConst {

        private IntentConst() {

        }

        public static final int CODE_REQUEST_LANDSCAPE = 10000;
    }

    public static final class DownLoadValues {

        private DownLoadValues() {

        }

        public static boolean IsNeedDownLoad = false;
        public static boolean IsDownLoadFinish = true;
        public static boolean IsDownLoadDialogShow = true;

        public static String DownLoadVersion = "";
        public static String DownLoadUrl = "";
        public static String DownLoading = "com.tjs.downloading";
        public static String DownLoadSuccess = "com.tjs.downloadsuccess";
        public static String DownLoadError = "com.tjs.downloaderror";
    }

}
