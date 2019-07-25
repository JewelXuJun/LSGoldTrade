package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

public class IntentUtils {

    public static void jumpSmall(Context context) {
        String appId = AppConfig.WECHATAPPID;
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfig.WEIXIN_SMALLROUTINE_ID;
        req.path = "pages/index/index?sid=&type=a";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    public static void jumpBankSmall(Context context) {
        String appId = AppConfig.WECHATAPPID;
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfig.WEIXIN_ICBC_SMALLROUTINE_ID;
        req.path = "pages/index/index?scene=10000000000001036500";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    public static void jumpHomeTab(Context context, List<NavigatorVo.NavigatorVoBean> usedModulesBeans, int position) {
        NavigatorVo.NavigatorVoBean usedModulesBean = usedModulesBeans.get(position);
        switch (usedModulesBean.getCode()) {
            case "MFKH":
                //免费开户
                jumpSmall(context);
                break;
            case "KSXD":
                //快速下单
                if (null == User.getInstance() || !User.getInstance().isLogin())
                    showNeedLoginDialog(context);
                else
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
                break;
            case "XSXT":
                //新手学堂
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.BEGINNERSACTIVITY)
                        .navigation();
                break;
            case "CJRL":
                //财经日历
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ECONOMICCALENDAR)
                        .navigation();
                break;
            case "HQYP":
                //行情研判
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.YANPAN)
                        .navigation();
                break;
            case "ZJHZ":
                //资金划转
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.CAPITALTRANSFER)
                        .navigation();
                break;
            case "DQCC":
                //当前持仓
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADEFRAGMENT_HOLD, null);
                break;
            case "DRWT":
                //当日委托
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.CURRENTENTRUST)
                        .navigation();
                break;
            case "DRCC":
                //当日持仓
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.CURRENTHOLDPOSITION)
                        .navigation();
                break;
            case "DRCJ":
                //当日成交
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.CURRENTDEAL)
                        .navigation();
                break;
            case "LSWT":
                //历史委托
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.HISTORYENTRUST)
                        .navigation();
                break;
            case "LSCJ":
                //历史成交
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.HISTORYDEAL)
                        .navigation();
                break;
            case "RJD":
                //日结单
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.DAILYSTATEMENT)
                        .navigation();
                break;
            case "WDDY":
                //我的订阅
                if (User.getInstance().isLogin()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.TRADINGBOX)
                            .navigation();
                } else {
                    showNeedLoginDialog(context);
                }
                break;
            case "LXKF":
                //联系客服
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.CUSTOMSERVICE)
                        .navigation();
                break;
            case "CD":
                //撤单
                if (null == User.getInstance() || !User.getInstance().isLogin())
                    showNeedLoginDialog(context);
                else
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCELORDERFRAGMENT, null);
                break;
            case "QB":
                //全部
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.FASTENTRY)
                        .navigation();
                break;
        }
    }

    private static void showNeedLoginDialog(Context mContext) {
        AlertDialog.Builder mDialog = null;
        if (null == mDialog) {
            mDialog = new AlertDialog.Builder(mContext);
            mDialog.setTitle(mContext.getResources().getString(R.string.text_tips));
            mDialog.setMessage(mContext.getResources().getString(R.string.login_message));
            mDialog.setPositiveButton(mContext.getResources().getString(R.string.text_login), (dialog, which) -> ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation());
            mDialog.setNegativeButton(mContext.getResources().getString(R.string.text_cancel), null);
            mDialog.show();
        } else {
            mDialog.show();
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
