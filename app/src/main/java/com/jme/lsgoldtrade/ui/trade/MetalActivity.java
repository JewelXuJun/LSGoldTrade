package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.ui.base.BaseActivity;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMetalBinding;

@Route(path = Constants.ARouterUriConst.METAL)
public class MetalActivity extends BaseActivity {

    private ActivityMetalBinding mBinding;

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
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

}
