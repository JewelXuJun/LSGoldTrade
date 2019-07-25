package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DateUtil;
import com.jme.common.util.RxBus;
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

    public static void IntentFastTab(String code) {
        User user = User.getInstance();

        switch (code) {
            case "MFKH":  //免费开户
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "KSXD":  //快速下单
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "XSXT":  //新手学堂
                ARouter.getInstance().build(Constants.ARouterUriConst.BEGINNERSACTIVITY).navigation();

                break;
            case "CJRL":  //财经日历
                ARouter.getInstance().build(Constants.ARouterUriConst.ECONOMICCALENDAR).navigation();

                break;
            case "HQYP":  //行情研判
                ARouter.getInstance().build(Constants.ARouterUriConst.MARKETJUDGMENT).navigation();

                break;
            case "ZJHZ":  //资金划转
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();

                break;
            case "DQCC":  //当前持仓
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADEFRAGMENT_HOLD, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

                break;
            case "DRWT":  //当日委托
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.CURRENTENTRUST).navigation();

                break;
            case "DRCC":  //当日持仓
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.CURRENTHOLDPOSITION).navigation();

                break;
            case "DRCJ":  //当日成交
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.CURRENTDEAL).navigation();

                break;
            case "LSWT":  //历史委托
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.HISTORYENTRUST).navigation();

                break;
            case "LSCJ":  //历史成交
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.HISTORYDEAL).navigation();

                break;
            case "RJD":   //日结单
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.DAILYSTATEMENT)
                            .withString("time", DateUtil.dataToStringWithData2(System.currentTimeMillis()))
                            .navigation();
                break;
            case "WDDY":  //我的订阅
                if (null == user || !user.isLogin())
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();

                break;
            case "LXKF":  //联系客服
                ARouter.getInstance().build(Constants.ARouterUriConst.CUSTOMSERVICE).navigation();

                break;
            case "CD":  //撤单
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCELORDERFRAGMENT, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                }

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

}
