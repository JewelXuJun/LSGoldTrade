package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.base.TabViewPagerAdapter;
import com.jme.lsgoldtrade.databinding.FragmentHoldPositionsBinding;

public class HoldPositionsFragment extends JMEBaseFragment {

    private FragmentHoldPositionsBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private TabViewPagerAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_hold_positions;
    }

    @Override
    protected void initView() {
        super.initView();

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
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentHoldPositionsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initTabs() {
        mTabTitles = new String[2];
        mTabTitles[0] = mContext.getResources().getString(R.string.transaction_current_positions);
        mTabTitles[1] = mContext.getResources().getString(R.string.transaction_current_entrust);

        mFragmentArrays = new Fragment[2];
        mFragmentArrays[0] = new CurrentPositionsFragment();
        mFragmentArrays[1] = new CurrentEntrustFragment();

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager(), mTabTitles, mFragmentArrays);

        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickTotalEquity() {

        }

        public void onClickGuaranteeFundSetting() {

        }

        public void onClickRiskRateTips() {

        }

        public void onClickDealQuery() {

        }

        public void onClickInOutMoney() {

        }

        public void onClickEntrustRiskManagement() {

        }

        public void onClickDailyStatementSheet() {

        }

        public void onClickConditionSheet() {

        }

    }
}
