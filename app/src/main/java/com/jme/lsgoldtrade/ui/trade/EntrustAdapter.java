package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class EntrustAdapter extends BaseQuickAdapter<OrderPageVo.OrderBean, BaseViewHolder> {

    public EntrustAdapter(int layoutResId, @Nullable List<OrderPageVo.OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderPageVo.OrderBean item) {
        if (null == item)
            return;

        int bsFlag = item.getBsFlag();

        helper.setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, item.getDeclarTime())
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, MarketUtil.getTradeDirectionColor(bsFlag))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setTextColor(R.id.tv_price, MarketUtil.getTradeDirectionColor(bsFlag))
                .setText(R.id.tv_entrust, String.valueOf(item.getEntrustNumber()))
                .setText(R.id.tv_surplus, String.valueOf(item.getRemnantNumber()))
                .setText(R.id.tv_state, MarketUtil.getEntrustState(item.getStatus()));
    }
}
