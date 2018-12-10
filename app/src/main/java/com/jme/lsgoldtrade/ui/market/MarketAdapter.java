package com.jme.lsgoldtrade.ui.market;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;

import java.util.List;

public class MarketAdapter extends BaseQuickAdapter<FiveSpeedVo, BaseViewHolder> {

    public MarketAdapter(int layoutResId, @Nullable List<FiveSpeedVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FiveSpeedVo item) {
        if (null == item)
            return;
    }
}
