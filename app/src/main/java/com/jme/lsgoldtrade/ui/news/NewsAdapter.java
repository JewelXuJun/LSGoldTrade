package com.jme.lsgoldtrade.ui.news;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NewsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (null == item)
            return;


    }
}
