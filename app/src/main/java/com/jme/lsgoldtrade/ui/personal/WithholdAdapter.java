package com.jme.lsgoldtrade.ui.personal;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.PayIconVo;

import java.util.List;

public class WithholdAdapter extends BaseQuickAdapter<PayIconVo, BaseViewHolder> {

    public WithholdAdapter(List<PayIconVo> data) {
        super(R.layout.item_withhold, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayIconVo item) {
        if (null == item)
            return;


    }
}

