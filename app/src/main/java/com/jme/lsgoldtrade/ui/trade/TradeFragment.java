package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentTradeBinding;

import rx.Subscription;

public class TradeFragment extends JMEBaseFragment {

    private FragmentTradeBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private PagerAdapter mAdapter;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentTradeBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_toolbar_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        initInfoTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private void initInfoTabs() {
        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.trade_hold_position);
        mTabTitles[1] = mContext.getResources().getString(R.string.market_declaration_form);
        mTabTitles[2] = mContext.getResources().getString(R.string.trade_cancel_order);
        mTabTitles[3] = mContext.getResources().getString(R.string.trade_query);

        mFragmentArrays = new Fragment[4];
        mFragmentArrays[0] = new HoldPositionFragment();
        mFragmentArrays[1] = new DeclarationFormFragment();
        mFragmentArrays[2] = new CancelOrderFragment();
        mFragmentArrays[3] = new QueryFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(4);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
        mBinding.tablayout.post(() -> setIndicator(mBinding.tablayout, 30, 30));
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RxBus_TradeFragment:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
            }
        });
    }

    public class ClickHandlers {

        public void onClickNews() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.NEWSACTIVITY)
                    .navigation();
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
