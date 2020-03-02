package com.jme.lsgoldtrade.ui.mainpage;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityEconomicCalendarBinding;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;

import rx.Subscription;

/**
 * 财经日历
 */
@Route(path = Constants.ARouterUriConst.ECONOMICCALENDAR)
public class EconomicCalendarActivity extends JMEBaseActivity {

    private ActivityEconomicCalendarBinding mBinding;
    private Subscription mRxbus;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_economic_calendar;
    }

    @Override
    protected void initView() {
        super.initView();

        setWebViewSettings();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.webview.loadUrl(Constants.HttpConst.URL_ECONOMIC_CALENDAR);
    }

    @Override
    protected void initListener() {
        super.initListener();
        initRxBus();
        mBinding.webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mBinding.webview.loadUrl(url);

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

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD:
                    getUserPasswordSettingInfo();

                    break;
            }
        });
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasTimeout = passwordInfoVo.getHasTimeout();
                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();
                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();
                    if (TextUtils.isEmpty(hasSettingDigital) || hasSettingDigital.equals("N")) {
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING, null);
                    } else {

                        if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N")){
                            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
                            return;
                        }


                        int type = 1;
                        if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                            boolean isCanUseFingerPrint = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (FingerprintManagerCompat.from(mContext).isHardwareDetected()
                                        && FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints())
                                    isCanUseFingerPrint = true;
                            }

                            if (isCanUseFingerPrint) {
                                type = 2;
                            } else {
                                if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y"))
                                    type = 3;
                                else
                                    type = 1;
                            }
                        } else if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y")) {
                            type = 3;
                        } else if (passwordInfoVo.getHasTimeout().equals("Y")) {
                            type = 1;
                        }
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
                                .withInt("Type", type)
                                .withInt("callEntry",5)
                                .navigation();
                    }
                }
                break;
        }
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityEconomicCalendarBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setWebViewSettings() {
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setUserAgentString(mBinding.webview.getSettings().getUserAgentString() + "LSGoldTradeAndroid");
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        mBinding.webview.getSettings().setDatabaseEnabled(true);
        mBinding.webview.getSettings().setUseWideViewPort(true);
        mBinding.webview.getSettings().setLoadWithOverviewMode(true);
        mBinding.webview.getSettings().setDisplayZoomControls(true);
        mBinding.webview.getSettings().setSupportZoom(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBinding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (Build.VERSION.SDK_INT >= 19)
            mBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    public class ClickHandlers {

        public void onClickBack() {
            finish();
        }

        public void onClickEconomicCalendar() {
            mBinding.tvEconomicCalendar.setBackground(ContextCompat.getDrawable(EconomicCalendarActivity.this, R.drawable.bg_btn_blue_solid_left));
            mBinding.tvEconomicCalendar.setTextColor(ContextCompat.getColor(EconomicCalendarActivity.this, R.color.white));
            mBinding.tvNews.setBackground(ContextCompat.getDrawable(EconomicCalendarActivity.this, R.drawable.bg_btn_white_solid_right));
            mBinding.tvNews.setTextColor(ContextCompat.getColor(EconomicCalendarActivity.this, R.color.color_blue_deep));

            mBinding.webview.loadUrl(Constants.HttpConst.URL_ECONOMIC_CALENDAR);
        }

        public void onClickEconomicNews() {
            mBinding.tvEconomicCalendar.setBackground(ContextCompat.getDrawable(EconomicCalendarActivity.this, R.drawable.bg_btn_white_solid_left));
            mBinding.tvEconomicCalendar.setTextColor(ContextCompat.getColor(EconomicCalendarActivity.this, R.color.color_blue_deep));
            mBinding.tvNews.setBackground(ContextCompat.getDrawable(EconomicCalendarActivity.this, R.drawable.bg_btn_blue_solid_right));
            mBinding.tvNews.setTextColor(ContextCompat.getColor(EconomicCalendarActivity.this, R.color.white));

            mBinding.webview.loadUrl(Constants.HttpConst.URL_NEWS);
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

        super.onDestroy();
        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
