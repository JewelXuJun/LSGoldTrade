package com.jme.lsgoldtrade.ui.transaction;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentInoutMoneyHfBinding;

public class InOutMoneyHFFragment extends JMEBaseFragment {

    private FragmentInoutMoneyHfBinding mBinding;

    private String mType;

    public static Fragment newInstance(String type) {
        InOutMoneyHFFragment fragment = new InOutMoneyHFFragment();

        Bundle bundle = new Bundle();
        bundle.putString("Type", type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_inout_money_hf;
    }

    @Override
    protected void initView() {
        super.initView();

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

        mType = getArguments().getString("Type");

        mBinding.webview.loadUrl("https://www.baidu.com");
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
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentInoutMoneyHfBinding) mBindingUtil;
    }

    @Override
    public void onDestroy() {
        mBinding.webview.onPause();
        mBinding.webview.destroy();

        super.onDestroy();
    }
}
