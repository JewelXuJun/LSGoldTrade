package com.jme.lsgoldtrade.ui.tradingbox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jme.lsgoldtrade.domain.TradingBoxVo;

import java.util.List;

public class TradingBoxFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<TradingBoxVo.TradingBoxListVoBean> mTradingBoxListVoBeanList;
    private List<TradingBoxFragment> mTradingBoxFragmentList;
    private String mType;

    public TradingBoxFragmentPagerAdapter(FragmentManager fragmentManager, List<TradingBoxVo.TradingBoxListVoBean> tradingBoxListVoBeanList,
                                          List<TradingBoxFragment> tradingBoxFragmentList, String type) {
        super(fragmentManager);

        mTradingBoxListVoBeanList = tradingBoxListVoBeanList;
        mTradingBoxFragmentList = tradingBoxFragmentList;
        mType = type;
    }

    @Override
    public Fragment getItem(int position) {
        TradingBoxFragment tradingBoxFragment = mTradingBoxFragmentList.get(position);
        tradingBoxFragment.setData(mTradingBoxListVoBeanList.get(position), mType);

        return tradingBoxFragment;
    }

    @Override
    public int getCount() {
        return null == mTradingBoxListVoBeanList ? 0 : mTradingBoxListVoBeanList.size();
    }

}
