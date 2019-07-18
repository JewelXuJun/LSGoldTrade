package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAgreementBinding;

/**
 * 各种协议
 */
@Route(path = Constants.ARouterUriConst.AGREEMENT)
public class AgreementActivity extends JMEBaseActivity {

    private ActivityAgreementBinding mBinding;

    private String url, title = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityAgreementBinding) mBindingUtil;
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        initToolbar(title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBinding.webview.loadUrl(url);
    }
}
