package com.jme.lsgoldtrade.ui.trade;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseActivity;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityMetalBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import com.jme.lsgoldtrade.service.PaymentService;
import com.jme.lsgoldtrade.util.PaymentHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.METAL)
public class MetalActivity extends BaseActivity {

    private ActivityMetalBinding mBinding;

    private IWXAPI mWxapi;
    private PaymentHelper mPaymentHelper;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_metal;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMetalBinding) mBindingUtil;

        initToolbar(R.string.trade_metal, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mWxapi = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID, true);
        mPaymentHelper = new PaymentHelper();

        UserInfoVo userInfoVo = User.getInstance().getCurrentUser();

        if (null != userInfoVo) {
            String token = userInfoVo.getToken();

            if (!TextUtils.isEmpty(token))
                updateData(token);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

        });

        mBinding.webview.setOnLongClickListener(v -> true);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();
            Object callObject = message.getObject2();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_METAL_PAY:
                    if (null != callObject)
                        gotoPayment(callObject.toString());

                    break;
                case Constants.RxBusConst.RXBUS_METAL_PAY_SUCCESS:
                    mBinding.webview.loadUrl("javascript:wechatPayCallBack()");

                    break;
            }
        });
    }


    private void updateData(String token) {
        setWebView();

        mBinding.webview.loadUrl(Constants.HttpConst.URL_METAL_PAY + token);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        mBinding.webview.getSettings().setDatabaseEnabled(true);
        mBinding.webview.getSettings().setUseWideViewPort(true);
        mBinding.webview.getSettings().setLoadWithOverviewMode(true);
        mBinding.webview.getSettings().setDisplayZoomControls(true);
        mBinding.webview.addJavascriptInterface(new JavaScriptInterface(), "TJSAndroid");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBinding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    public void setBackNavigation(boolean hasBack) {
        mToolbarHelper.setBackNavigation(hasBack, v -> onBackPressed());
    }

    private void gotoPayment(String value) {
        if (mWxapi.isWXAppInstalled())
            getGoldGoodsPay(value);
        else
            showShortToast(R.string.text_wechat_uninstalled);
    }

    private void getGoldGoodsPay(String value) {
        String goodsDesc = null;
        String orderNo = null;
        String totalFee = null;

        try {
            JSONObject result = new JSONObject(value);

            goodsDesc = result.getString("goodsDesc");
            orderNo = result.getString("orderNo");
            totalFee = result.getString("totalFee");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(goodsDesc) || TextUtils.isEmpty(orderNo) || TextUtils.isEmpty(totalFee))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("goodsDesc", goodsDesc);
        params.put("orderNo", orderNo);
        params.put("totalFee", totalFee);

        sendRequest(PaymentService.getInstance().goldGoodsPay, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GoldGoodsPay":
                if (head.isSuccess()) {
                    WechatPayVo wechatPayVo;

                    try {
                        wechatPayVo = (WechatPayVo) response;
                    } catch (Exception e) {
                        wechatPayVo = null;

                        e.printStackTrace();
                    }

                    if (null == wechatPayVo)
                        return;

                    mPaymentHelper.startWeChatPay(this, wechatPayVo, "GoldGoodsPay");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.webview.canGoBack())
            mBinding.webview.goBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mBinding.webview.onPause();
        mBinding.webview.destroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();

        super.onDestroy();
    }

}
