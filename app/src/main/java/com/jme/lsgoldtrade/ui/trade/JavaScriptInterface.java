package com.jme.lsgoldtrade.ui.trade;

import android.webkit.JavascriptInterface;

import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.config.Constants;

public class JavaScriptInterface {

    @JavascriptInterface
    public void androidReply(String data) {
        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_METAL_PAY, data);
    }

}
