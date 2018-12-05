package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
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

    public class ClickHandlers {

        public void ClickNetWorkSeconds(int seconds) {

        }

        public void ClickWiFiSeconds(int seconds) {

        }

    }
}
