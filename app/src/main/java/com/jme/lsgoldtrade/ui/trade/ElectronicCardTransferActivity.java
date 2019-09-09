package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityElectronicCardTransferBinding;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.ELECTRONICCARDTRANSFER)
public class ElectronicCardTransferActivity extends JMEBaseActivity {

    private ActivityElectronicCardTransferBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private String mElectronicAccounts;
    private String mRelevanceId;

    private PagerAdapter mAdapter;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_electronic_card_transfer;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_transfer_icbc_electronic, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mElectronicAccounts = getIntent().getStringExtra("ElectronicAccounts");
        mRelevanceId = getIntent().getStringExtra("RelevanceId");

        mAdapter = new TabViewPagerAdapter(getSupportFragmentManager());

        initInfoTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityElectronicCardTransferBinding) mBindingUtil;
    }

    protected void onPause() {
        super.onPause();

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CAPITALTRANSFER_SUCCESS, null);
    }

    private void initInfoTabs() {
        mTabTitles = new String[3];
        mTabTitles[0] = getString(R.string.trade_transfer_icbc_electronic_card_in);
        mTabTitles[1] = getString(R.string.trade_transfer_icbc_electronic_card_out);
        mTabTitles[2] = getString(R.string.trade_transfer_icbc_electronic_card_detail);

        mFragmentArrays = new Fragment[3];
        mFragmentArrays[0] = ElectronicCardMoneyInFragment.newInstance(mElectronicAccounts, mRelevanceId);
        mFragmentArrays[1] = new ElectronicCardMoneyOutFragment();
        mFragmentArrays[2] = new ElectronicCardDetailFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);

        mBinding.tabViewpager.setCurrentItem(!TextUtils.isEmpty(AppConfig.TransferType) && AppConfig.TransferType.equals("TransferOut") ? 1 : 0);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_ELECTRONICCARD_INOUT_SUCCESS:
                    finish();

                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
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
