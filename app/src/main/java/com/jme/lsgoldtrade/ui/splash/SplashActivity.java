package com.jme.lsgoldtrade.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivitySplashBinding;
import com.jme.lsgoldtrade.ui.main.MainActivity;

@Route(path = Constants.ARouterUriConst.SPLASH)
public class SplashActivity extends JMEBaseActivity {

    private ActivitySplashBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivitySplashBinding) mBindingUtil;

        new Handler().postDelayed(() -> gotoMainActivity(), Constants.SPLASH_DELAY_MILLIS);
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
    }

    private void gotoMainActivity() {
        startAnimActivity(MainActivity.class);

        finish();
    }

}
