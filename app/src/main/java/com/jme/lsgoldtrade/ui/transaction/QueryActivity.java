package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.base.TabViewPagerAdapter;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityQueryBinding;

@Route(path = Constants.ARouterUriConst.QUERY)
public class QueryActivity extends JMEBaseActivity {

    private ActivityQueryBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private TabViewPagerAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_query;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_query, true);

        initTabs();
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

        mBinding = (ActivityQueryBinding) mBindingUtil;
    }

    private void initTabs() {
        mTabTitles = new String[3];
        mTabTitles[0] = getResources().getString(R.string.transaction_today_deal);
        mTabTitles[1] = getResources().getString(R.string.transaction_historical_deal);
        mTabTitles[2] = getResources().getString(R.string.transaction_historical_entrust);

        mFragmentArrays = new Fragment[3];
        mFragmentArrays[0] = new TodayDealFragment();
        mFragmentArrays[1] = new HistoricalDealFragment();
        mFragmentArrays[2] = new HistoricalEntrustFragment();

        mAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), mTabTitles, mFragmentArrays);

        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }
}
