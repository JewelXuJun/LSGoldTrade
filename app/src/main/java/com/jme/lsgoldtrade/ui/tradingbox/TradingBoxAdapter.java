package com.jme.lsgoldtrade.ui.tradingbox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jme.lsgoldtrade.domain.TradingBoxDataInfoVo;

import java.util.List;

public class TradingBoxAdapter extends FragmentPagerAdapter {

    private List<TradingBoxDataInfoVo.HistoryVoBean> mHistoryVoBeanList;
    private List<TradingBoxFragment> mTradingBoxFragmentList;

    public TradingBoxAdapter(FragmentManager fm, List<TradingBoxDataInfoVo.HistoryVoBean> historyVoBeanList, List<TradingBoxFragment> tradingBoxFragmentList) {
        super(fm);

        mHistoryVoBeanList = historyVoBeanList;
        mTradingBoxFragmentList = tradingBoxFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        TradingBoxFragment tradingBoxFragment = mTradingBoxFragmentList.get(position);
        tradingBoxFragment.setData(mHistoryVoBeanList.get(position));

        return tradingBoxFragment;
    }

    @Override
    public int getCount() {
        return null == mHistoryVoBeanList ? 0 : mHistoryVoBeanList.size();
    }

}
