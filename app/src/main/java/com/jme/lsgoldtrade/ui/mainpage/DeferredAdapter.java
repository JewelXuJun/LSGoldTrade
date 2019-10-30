package com.jme.lsgoldtrade.ui.mainpage;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DeferredAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public DeferredAdapter(int resId, List<String> list) {
        super(resId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
