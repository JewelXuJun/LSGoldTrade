package com.jme.lsgoldtrade.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.ui.base.BaseActivity;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;

import butterknife.BindView;

/**
 * Created by XuJun on 2018/4/24.
 */

@Route(path = Constants.ARouterUriConst.RFINEXWEBVIEW)
public class JMEBaseWebView extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_close)
    TextView tv_close;

    protected WebView webview;

    protected String mTitle = null;
    protected String mUrl = null;

    private ProgressBar progressBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        webview = findViewById(R.id.webview);

        progressBar = findViewById(R.id.pb);

        tv_close.setVisibility(View.GONE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUserAgentString(webview.getSettings().getUserAgentString() + " LSGoldTradeAndroid");
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDisplayZoomControls(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (Build.VERSION.SDK_INT >= 19)
            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        mTitle = bundle.getString("title");
        mUrl = bundle.getString("url");

        if (!TextUtils.isEmpty(mUrl))
            updateData(mUrl);

        if (!TextUtils.isEmpty(mTitle))
            initToolbar(mTitle, true, getResources().getColor(R.color.black));
        else
            initToolbar("", true, getResources().getColor(R.color.black));
    }

    @Override
    protected void initListener() {
        tv_close.setOnClickListener(this);

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (progressBar != null && newProgress != 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
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

        //禁止copy
        webview.setOnLongClickListener(v -> true);
    }

    private void updateData(String url) {
        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
    }

    public void setBackNavigation(boolean hasBack) {
        mToolbarHelper.setBackNavigation(hasBack, v -> onBackPressed());
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        webview.onPause();
        webview.destroy();
        webview = null;

        super.onDestroy();
    }
}