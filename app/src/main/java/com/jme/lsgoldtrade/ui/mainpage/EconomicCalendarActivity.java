package com.jme.lsgoldtrade.ui.mainpage;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.util.AppManager;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityEconomicCalendarBinding;
import com.jme.lsgoldtrade.ui.trade.CancelOrderFragment;
import com.jme.lsgoldtrade.ui.trade.DeclarationFormFragment;
import com.jme.lsgoldtrade.ui.trade.HoldPositionFragment;
import com.jme.lsgoldtrade.ui.trade.QueryFragment;

import rx.Subscription;

/**
 * 财经日历
 */
@Route(path = Constants.ARouterUriConst.ECONOMICCALENDAR)
public class EconomicCalendarActivity extends JMEBaseActivity {

    private ActivityEconomicCalendarBinding mBinding;

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
    }

}
