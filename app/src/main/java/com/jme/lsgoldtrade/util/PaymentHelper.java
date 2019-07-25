package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.widget.Toast;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/25 10:10
 * Desc   : 支付宝和微信支付工具类
 */
public class PaymentHelper {

    /**
     * 微信支付
     */
    public void startWeChatPay(Activity activity, WechatPayVo wechatPayVo) {
        if (activity == null || wechatPayVo == null)
            return;
        if (!AppConfig.WECHATAPPID.equals(wechatPayVo.getAppid()))
            return;

        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, AppConfig.WECHATAPPID, true);
        // 将该app注册到微信
        wxapi.registerApp(AppConfig.WECHATAPPID);
        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(activity, "你没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        // 我们把请求到的参数全部给微信
        PayReq req = new PayReq(); //调起微信APP的对象
        req.appId = AppConfig.WECHATAPPID;
        req.partnerId = wechatPayVo.getPartnerid();
        req.prepayId = wechatPayVo.getPrepayid();
        req.nonceStr = wechatPayVo.getNoncestr();
        req.timeStamp = wechatPayVo.getTimestamp();
        req.packageValue = wechatPayVo.getPackageX(); //Sign=WXPay
        req.sign = wechatPayVo.getSign();

        wxapi.sendReq(req); //发送调起微信的请求
    }

}
