package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.domain.OrderPageVo;

import java.util.List;

public class CancelOrderAdapter extends BaseQuickAdapter<OrderPageVo.OrderBean, BaseViewHolder> {

    public CancelOrderAdapter(Context context, int layoutResId, @Nullable List<OrderPageVo.OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderPageVo.OrderBean item) {

    }
}
