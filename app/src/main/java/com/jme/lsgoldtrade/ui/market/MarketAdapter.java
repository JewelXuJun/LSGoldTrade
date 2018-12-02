package com.jme.lsgoldtrade.ui.market;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MarketAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MarketAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (null == item)
            return;
    }
}
