package com.jme.lsgoldtrade.ui.tradingbox;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jme.lsgoldtrade.domain.TradingBoxVo;

import java.util.List;

public class TradingBoxFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<TradingBoxVo.TradingBoxListVoBean> mTradingBoxListVoBeanList;
    private List<TradingBoxFragment> mTradingBoxFragmentList;
    private String mPeriodName;
    private String mType;

    public TradingBoxFragmentPagerAdapter(FragmentManager fragmentManager, List<TradingBoxVo.TradingBoxListVoBean> tradingBoxListVoBeanList,
                                          List<TradingBoxFragment> tradingBoxFragmentList, String periodName, String type) {
        super(fragmentManager);

        mTradingBoxListVoBeanList = tradingBoxListVoBeanList;
        mTradingBoxFragmentList = tradingBoxFragmentList;
        mPeriodName = periodName;
        mType = type;
    }

    @Override
    public Fragment getItem(int position) {
        TradingBoxFragment tradingBoxFragment = mTradingBoxFragmentList.get(position);
        tradingBoxFragment.setData(mPeriodName, mTradingBoxListVoBeanList, mTradingBoxListVoBeanList.get(position), position, mType);

        return tradingBoxFragment;
    }

    @Override
    public int getCount() {
        return null == mTradingBoxListVoBeanList ? 0 : mTradingBoxListVoBeanList.size();
    }

}
