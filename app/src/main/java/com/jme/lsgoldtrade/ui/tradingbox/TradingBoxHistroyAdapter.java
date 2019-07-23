package com.jme.lsgoldtrade.ui.tradingbox;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryItemSimpleVo;

import java.util.List;

public class TradingBoxHistroyAdapter extends BaseQuickAdapter<TradingBoxHistoryItemSimpleVo, BaseViewHolder> {

    public TradingBoxHistroyAdapter(int layoutResId, List<TradingBoxHistoryItemSimpleVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingBoxHistoryItemSimpleVo item) {
        if (null == item)
            return;

        String direction = item.getDirection();
        String pushTime = item.getPushTime();

        helper.setText(R.id.tv_number, String.format(mContext.getResources().getString(R.string.trading_box_number_title), item.getPeriodName()))
                .setText(R.id.tv_abstract, String.format(mContext.getResources().getString(R.string.trading_box_variety),
                        mContext.getResources().getString(R.string.trading_box_chance), item.getChance()))
                .setText(R.id.tv_contract, item.getVariety())
                .setText(R.id.tv_direction, TextUtils.isEmpty(direction) ? ""
                        : direction.equals("0") ? mContext.getResources().getString(R.string.text_more) : mContext.getResources().getString(R.string.text_empty))
                .setText(R.id.tv_publish_time, TextUtils.isEmpty(pushTime) ? "" : String.format(mContext.getResources().getString(R.string.trading_box_publist_time),
                        DateUtil.dataToStringWithData2(DateUtil.dateToLong(pushTime))));
    }
}
