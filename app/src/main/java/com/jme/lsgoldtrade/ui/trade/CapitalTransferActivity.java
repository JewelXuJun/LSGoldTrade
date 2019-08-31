package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCapitalTransferBinding;

/**
 * 资金划转
 */
@Route(path = Constants.ARouterUriConst.CAPITALTRANSFER)
public class CapitalTransferActivity extends JMEBaseActivity {

    private ActivityCapitalTransferBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private PagerAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_capital_transfer;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_capital_transfer, true);
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

        mBinding = (ActivityCapitalTransferBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setElectronicCardLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CAPITALTRANSFER_SUCCESS, null);
    }

    private void initInfoTabs() {
        mTabTitles = new String[3];
        mTabTitles[0] = getString(R.string.trade_money_in);
        mTabTitles[1] = getString(R.string.trade_money_out);
        mTabTitles[2] = getString(R.string.trade_turnover);

        mFragmentArrays = new Fragment[3];
        mFragmentArrays[0] = new MoneyInFragment();
        mFragmentArrays[1] = new MoneyOutFragment();
        mFragmentArrays[2] = new TurnOverFragment();

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

    private void setElectronicCardLayout() {
        if (null == mUser || null == mUser.getCurrentUser()) {
            mBinding.layoutElectronicCard.setVisibility(View.GONE);
        } else {
            String cardType = mUser.getCurrentUser().getCardType();

            mBinding.layoutElectronicCard.setVisibility(!TextUtils.isEmpty(cardType) && cardType.equals("2")
                    ? View.VISIBLE : View.GONE);
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

    public class ClickHandlers {

        public void onClickTips() {

        }

        public void onClickUpdate() {

        }

        public void onClickTransferIn() {
            ARouter.getInstance().build(Constants.ARouterUriConst.ELECTRONICCARDTRANSFER).navigation();
        }

        public void onClickTransferOut() {
            ARouter.getInstance().build(Constants.ARouterUriConst.ELECTRONICCARDTRANSFER).navigation();
        }

    }

}
