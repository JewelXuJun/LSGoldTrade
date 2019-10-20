package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityFingerprintBinding;

@Route(path = Constants.ARouterUriConst.FINGERPRINT)
public class FingerprintActivity extends JMEBaseActivity {

    private ActivityFingerprintBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fingerprint;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_fingerprint_setting, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityFingerprintBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
