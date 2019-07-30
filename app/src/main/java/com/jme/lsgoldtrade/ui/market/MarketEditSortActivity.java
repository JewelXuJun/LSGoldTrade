package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketEditSortBinding;

@Route(path = Constants.ARouterUriConst.MARKETEDITSORT)
public class MarketEditSortActivity extends JMEBaseActivity {

    private ActivityMarketEditSortBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_edit_sort;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.market_edit_sort, true);

        setRightNavigation(getString(R.string.text_save), 0, R.style.ToolbarThemeBlue, null);
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

        mBinding = (ActivityMarketEditSortBinding) mBindingUtil;
    }
}
