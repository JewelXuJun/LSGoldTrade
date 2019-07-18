package com.jme.lsgoldtrade.util;

import android.content.Context;

import com.jme.lsgoldtrade.config.AppConfig;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class JumpSmallRoutine {

    public static void jump(Context context) {
        String appId = AppConfig.WECHATAPPID; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfig.WEIXIN_XIAOCHENGXU_ID;         // 填小程序原始id
        req.path = "pages/index/index?sid=&type=a";                   //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

}
