package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityInoutMoneyHfBinding;

@Route(path = Constants.ARouterUriConst.INOUTMONEYHF)
public class InOutMoneyHFActivity extends JMEBaseActivity {

    private ActivityInoutMoneyHfBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private PagerAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_inout_money_hf;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_inoutmoney, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getSupportFragmentManager());

        initInfoTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityInoutMoneyHfBinding) mBindingUtil;
    }

    private void initInfoTabs() {
        mTabTitles = new String[3];
        mTabTitles[0] = getString(R.string.transaction_money_in);
        mTabTitles[1] = getString(R.string.transaction_money_out);
        mTabTitles[2] = getString(R.string.transaction_turnover);

        mFragmentArrays = new Fragment[3];
        mFragmentArrays[0] = InOutMoneyHFFragment.newInstance("In");
        mFragmentArrays[1] = InOutMoneyHFFragment.newInstance("Out");
        mFragmentArrays[2] = InOutMoneyHFFragment.newInstance("TrunOver");

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    final class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }
}
