package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketRefreshBinding;

@Route(path = Constants.ARouterUriConst.MARKETREFRESH)
public class MarketRefreshActivity extends JMEBaseActivity {

    private ActivityMarketRefreshBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_refresh;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMarketRefreshBinding) mBindingUtil;

        initToolbar(R.string.setting_refresh_rate, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        setNetWorkSeconds(AppConfig.TimeInterval_NetWork);
        setWiFiSeconds(AppConfig.TimeInterval_WiFi);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreUtils.setLong(this, SharedPreUtils.TimeInterval_NetWork, AppConfig.TimeInterval_NetWork);
        SharedPreUtils.setLong(this, SharedPreUtils.TimeInterval_WiFi, AppConfig.TimeInterval_WiFi);
    }

    private void setNetWorkSeconds(long seconds) {
        mBinding.imgNetwork5s.setVisibility(seconds == AppConfig.Second5 ? View.VISIBLE : View.GONE);
        mBinding.imgNetwork10s.setVisibility(seconds == AppConfig.Second10 ? View.VISIBLE : View.GONE);
        mBinding.imgNetwork15s.setVisibility(seconds == AppConfig.Second15 ? View.VISIBLE : View.GONE);

        AppConfig.TimeInterval_NetWork = seconds;
    }

    private void setWiFiSeconds(long seconds) {
        mBinding.imgWifi1s.setVisibility(seconds == AppConfig.Second ? View.VISIBLE : View.GONE);
        mBinding.imgWifi2s.setVisibility(seconds == AppConfig.Second2 ? View.VISIBLE : View.GONE);
        mBinding.imgWifi5s.setVisibility(seconds == AppConfig.Second5 ? View.VISIBLE : View.GONE);

        AppConfig.TimeInterval_WiFi = seconds;
    }

    public class ClickHandlers {

        public void ClickNetWorkSeconds(long seconds) {
            setNetWorkSeconds(seconds);
        }

        public void ClickWiFiSeconds(long seconds) {
            setWiFiSeconds(seconds);
        }

    }
}
