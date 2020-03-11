package com.jme.lsgoldtrade.ui.transaction;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOpenAccountHfBinding;

@Route(path = Constants.ARouterUriConst.OPENACCOUNTHFWEBVIEW)
public class OpenAccountHFWebviewActivity extends JMEBaseActivity {

    private ActivityOpenAccountHfBinding mBinding;

    private TextView tvClose;

    private String mUrl = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_open_account_hf;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_open_account_hf, false);

        tvClose = findViewById(R.id.tv_close);
        tvClose.setTextColor(ContextCompat.getColor(this, R.color.gray));
        tvClose.setVisibility(View.VISIBLE);

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

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mUrl = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(mUrl))
            updateData(mUrl);
    }

    @Override
    protected void initListener() {
        super.initListener();

        tvClose.setOnClickListener((v) -> finish());

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

        mBinding = (ActivityOpenAccountHfBinding) mBindingUtil;
    }

    private void updateData(String url) {
        mBinding.webview.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        mBinding.webview.onPause();
        mBinding.webview.destroy();

        super.onDestroy();
    }
}