package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.widget.Toast;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class PaymentHelper {

    public void startWeChatPay(Activity activity, WechatPayVo wechatPayVo, String type) {
        if (null == activity || null == wechatPayVo)
            return;

        if (!AppConfig.WECHATAPPID.equals(wechatPayVo.getAppid()))
            return;

        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, AppConfig.WECHATAPPID, true);
        wxapi.registerApp(AppConfig.WECHATAPPID);

        if (wxapi.isWXAppInstalled()) {
            PayReq payReq = new PayReq();
            payReq.appId = AppConfig.WECHATAPPID;
            payReq.partnerId = wechatPayVo.getPartnerid();
            payReq.prepayId = wechatPayVo.getPrepayid();
            payReq.nonceStr = wechatPayVo.getNoncestr();
            payReq.timeStamp = wechatPayVo.getTimestamp();
            payReq.packageValue = wechatPayVo.getPackageX();
            payReq.sign = wechatPayVo.getSign();
            payReq.extData = type;

            wxapi.sendReq(payReq);
        } else {
            Toast.makeText(activity, R.string.text_wechat_uninstalled, Toast.LENGTH_SHORT).show();
        }
    }

}
