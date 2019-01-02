package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.domain.PositionPageVo;

import java.util.List;

public class HoldPositionAdapter extends BaseQuickAdapter<PositionPageVo.PositionBean, BaseViewHolder> {

    public HoldPositionAdapter(int layoutResId, @Nullable List<PositionPageVo.PositionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionPageVo.PositionBean item) {

    }
}
