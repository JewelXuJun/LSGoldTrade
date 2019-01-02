package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.DealHistoryPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class DealHistoryAdapter extends BaseQuickAdapter<DealHistoryPageVo.DealHistoryBean, BaseViewHolder> {

    private String mDate = "";


    public DealHistoryAdapter(int layoutResId, @Nullable List<DealHistoryPageVo.DealHistoryBean> data) {
        super(layoutResId, data);
    }

    public void clearDate() {
        mDate = "";
    }

    @Override
    protected void convert(BaseViewHolder helper, DealHistoryPageVo.DealHistoryBean item) {
        if (null == item)
            return;

        String date = item.getMatchDate();
        int bsFlag = item.getBsFlag();

        helper.setText(R.id.tv_date, date)
                .setGone(R.id.tv_date, !mDate.equals(date))
                .setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, item.getMatchTime())
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, MarketUtil.getTradeDirectionColor(bsFlag))
                .setText(R.id.tv_amount, String.valueOf(item.getMatchHand()))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setText(R.id.tv_turn_volume, MarketUtil.decimalFormatMoney(item.getAmountStr()));

        mDate = date;
    }
}
