package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class DealAdapter extends BaseQuickAdapter<DealPageVo.DealBean, BaseViewHolder> {

    public DealAdapter(int layoutResId, @Nullable List<DealPageVo.DealBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DealPageVo.DealBean item) {
        if (null == item)
            return;

        int bsFlag = item.getBsFlag();

        helper.setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, item.getMatchTime())
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, MarketUtil.getTradeDirectionColor(bsFlag))
                .setText(R.id.tv_amount, String.valueOf(item.getMatchHand()))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setText(R.id.tv_turn_volume, MarketUtil.decimalFormatMoney(item.getAmountStr()));


    }
}
