package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.util.AppManager;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityEconomicCalendarBinding;
import com.jme.lsgoldtrade.ui.trade.CancelOrderFragment;
import com.jme.lsgoldtrade.ui.trade.DeclarationFormFragment;
import com.jme.lsgoldtrade.ui.trade.HoldPositionFragment;
import com.jme.lsgoldtrade.ui.trade.QueryFragment;

import rx.Subscription;

/**
 * 财经日历
 */
@Route(path = Constants.ARouterUriConst.ECONOMICCALENDAR)
public class EconomicCalendarActivity extends JMEBaseActivity {

    private ActivityEconomicCalendarBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private TabViewPagerAdapter mAdapter;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_economic_calendar;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityEconomicCalendarBinding) mBindingUtil;
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
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
        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initInfoTabs() {
        mTabTitles = new String[2];
        mTabTitles[0] = "财经日历";
        mTabTitles[1] = "快讯";

        mFragmentArrays = new Fragment[2];
        mFragmentArrays[0] = new CaiJingRiLiFragment();
        mFragmentArrays[1] = new KuaiXunFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(1);
        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
        mBinding.tablayout.post(() -> setIndicator(mBinding.tablayout, 0, 00));
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRADEFRAGMENT:
                    runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
            }
        });
    }

    public class ClickHandlers {

        public void onClickback() {
            AppManager.getAppManager().finishActivity();
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
