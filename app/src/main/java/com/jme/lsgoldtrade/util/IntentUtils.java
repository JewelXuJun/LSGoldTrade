package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DateUtil;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

public class IntentUtils {

    public static void intentSmallProcedures(Context context) {
        String appId = AppConfig.WECHATAPPID;
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfig.WEIXIN_SMALLROUTINE_ID;
        req.path = "pages/index/index?sid=&type=a";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    public static void intentICBCSmall(Context context) {
        String appId = AppConfig.WECHATAPPID;
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfig.WEIXIN_ICBC_SMALLROUTINE_ID;
        req.path = "pages/index/index?scene=10000000000001036500";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    public static void IntentFastTab(Context context, String code) {
        User user = User.getInstance();

        switch (code) {
            case "MFKH":  //免费开户
            case "KSXD":  //快速下单
                if (null == user || !user.isLogin()) {
                    gotoLogin(context);
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "XSXT":  //新手学堂
                ARouter.getInstance().build(Constants.ARouterUriConst.BEGINNERSACTIVITY).navigation();

                break;
            case "CJRL":  //财经日历

//                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CJRL_SETPASSWORD, null);
                ARouter.getInstance().build(Constants.ARouterUriConst.ECONOMICCALENDAR).navigation();

                break;
            case "HQYP":  //行情研判
//                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_HQYP_SETPASSWORD, null);
                ARouter.getInstance().build(Constants.ARouterUriConst.MARKETJUDGMENT).navigation();

                break;
            case "ZJHZ":  //资金划转
                if (null == user || !user.isLogin()) {
                    gotoLogin(context);
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_ZJHZ_SETPASSWORD, null);
//                    if (!TextUtils.isEmpty(user.getIsFromTjs()) && user.getIsFromTjs().equals("true")) {
//                        if (user.getCurrentUser().getCardType().equals("2") && user.getCurrentUser().getReserveFlag().equals("N"))
//                            ARouter.getInstance().build(Constants.ARouterUriConst.BANKRESERVE).navigation();
//                        else
//                            ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
//                    } else {
//                        ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
//                    }
                }

                break;
            case "DQCC":  //当前持仓
                if (null == user || !user.isLogin()) {
                    gotoLogin(context);
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "DRCJ":  //当日成交
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.QUERY)
                            .withInt("Type", 0)
                            .navigation();

                break;
            case "LSWT":  //历史委托
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.QUERY)
                            .withInt("Type", 2)
                            .navigation();

                break;
            case "LSCJ":  //历史成交
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.QUERY)
                            .withInt("Type", 1)
                            .navigation();

                break;
            case "RJD":   //日结单
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.DAILYSTATEMENT).navigation();
                break;
            case "WDDY":  //我的订阅
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_WDDY_SETPASSWORD, null);
//                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();

                break;
            case "LXKF":  //联系客服
                ARouter.getInstance().build(Constants.ARouterUriConst.CUSTOMSERVICE).navigation();

                break;
            case "DRWT":  //当日委托
            case "CD":  //撤单
                if (null == user || !user.isLogin()) {
                    gotoLogin(context);
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CANCEL_ORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "SWGJS": //实物贵金属
                if (null == user || !user.isLogin())
                    gotoLogin(context);
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.METAL).navigation();

                break;
            case "QB":  //全部
                ARouter.getInstance().build(Constants.ARouterUriConst.FASTMANAGEMENT).navigation();

                break;
        }
    }

    public static boolean isWeChatAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);

        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                if (pn.equals("com.tencent.mm"))
                    return true;
            }
        }

        return false;
    }

    private static void gotoLogin(Context context) {
        String loginType = SharedPreUtils.getString(context, SharedPreUtils.Login_Type);

        if (TextUtils.isEmpty(loginType)) {
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        } else {
            if (loginType.equals("Account"))
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (loginType.equals("Mobile"))
                ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
        }
    }

}
