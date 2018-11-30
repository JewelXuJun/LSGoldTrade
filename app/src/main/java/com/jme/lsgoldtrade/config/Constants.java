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

        public static int Envi = Produce;

        private HttpConst() {

        }

        public static String URL_BASE;

        static {
            if (Envi == Test_WAN) {
                URL_BASE = "";
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

        public static final int MSG_UPDATE_DATA = 0;
        public static final int MSG_RELOAD_DATA = 1;
    }

    public static final class ARouterUriConst {
        public static final String RFINEXWEBVIEW = "/JMEBase/JMEBaseWebviewActivity";
        public static final String LOGIN = "/PersonalFragment/LoginActivity";
        public static final String MAIN = "/LoginActivity/MainActivity";
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
