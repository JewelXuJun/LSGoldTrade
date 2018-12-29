package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;

import java.util.List;

public class TurnOverAdapter extends BaseQuickAdapter<InOutTurnOverVo.TurnOverBean, BaseViewHolder> {

    public TurnOverAdapter(int layoutResId, @Nullable List<InOutTurnOverVo.TurnOverBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InOutTurnOverVo.TurnOverBean item) {

    }
}
