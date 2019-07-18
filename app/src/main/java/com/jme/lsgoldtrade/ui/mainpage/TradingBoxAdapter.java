package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.domain.TradingBoxVo;

import java.util.List;

public class TradingBoxAdapter  extends FragmentPagerAdapter {

    private Context context;

    private List<TradingBoxVo.HistoryListVoListBean> list;

    private List<JMEBaseFragment> listFragment;

    public TradingBoxAdapter(FragmentManager fm, Context context, List<TradingBoxVo.HistoryListVoListBean> list, List<JMEBaseFragment> listFragment) {
        super(fm);
        this.context = context;
        this.list = list;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        TradingBoxFragment tradingBoxFragment = (TradingBoxFragment) listFragment.get(position);
        tradingBoxFragment.setData(list, position);
        return tradingBoxFragment;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }
}
