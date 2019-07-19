package com.jme.lsgoldtrade.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.ui.base.BaseActivity;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.ui.main.MainActivity;

@Route(path = Constants.ARouterUriConst.SPLASH)
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();

        new Handler().postDelayed(() -> gotoMainActivity(), Constants.SPLASH_DELAY_MILLIS);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        AppConfig.TimeInterval_NetWork = SharedPreUtils.getLong(this, SharedPreUtils.TimeInterval_NetWork, AppConfig.Second5);
        AppConfig.TimeInterval_WiFi = SharedPreUtils.getLong(this, SharedPreUtils.TimeInterval_WiFi, AppConfig.Second2);
        AppConfig.Select_ContractId = "Ag(T+D)";
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void gotoMainActivity() {
        startAnimActivity(MainActivity.class);

        finish();
    }

}
