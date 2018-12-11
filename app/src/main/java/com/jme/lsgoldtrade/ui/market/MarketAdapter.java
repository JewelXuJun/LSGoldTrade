package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.List;

public class MarketAdapter extends BaseQuickAdapter<FiveSpeedVo, BaseViewHolder> {

    private Context mContext;

    public MarketAdapter(Context context, int layoutResId, @Nullable List<FiveSpeedVo> data) {
        super(layoutResId, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FiveSpeedVo item) {
        if (null == item)
            return;

        String contractId = item.getContractId();

        if (TextUtils.isEmpty(contractId))
            return;

        long latestPrice = item.getLatestPrice();
        long closePrice = item.getClosePrice();
        long upDownRate = item.getUpDownRate();
        long turnVolume = item.getTurnVolume();

        int priceType = new BigDecimal(latestPrice).compareTo(new BigDecimal(closePrice));
        int rateType = new BigDecimal(upDownRate).compareTo(new BigDecimal(0));

        helper.setText(R.id.tv_contractname_cn, MarketUtil.getContractNameCN(contractId))
                .setText(R.id.tv_contractname_en, MarketUtil.getContractNameEN(contractId))
                .setText(R.id.tv_last_price, String.valueOf(latestPrice))
                .setTextColor(R.id.tv_last_price, MarketUtil.getMarketStateColor(mContext, priceType))
                .setText(R.id.tv_rate, MarketUtil.getMarketRateValue(mContext, rateType, upDownRate))
                .setTextColor(R.id.tv_rate, MarketUtil.getMarketStateColor(mContext, rateType))
                .setText(R.id.tv_volume, MarketUtil.getVolumeValue(String.valueOf(turnVolume), false));
    }
}
