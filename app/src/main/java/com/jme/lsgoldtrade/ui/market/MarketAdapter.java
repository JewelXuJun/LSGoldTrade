package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

        String latestPrice = item.getLatestPriceValue();
        String upDownRate = item.getUpDownRateValue();
        int rateType;

        if (TextUtils.isEmpty(upDownRate))
            rateType = 0;
        else
            rateType = new BigDecimal(upDownRate).compareTo(new BigDecimal(0));

        helper.setText(R.id.tv_contractname_cn, MarketUtil.getContractNameCN(contractId))
                .setText(R.id.tv_contractname_en, contractId)
                .setText(R.id.tv_last_price, latestPrice)
                .setTextColor(R.id.tv_last_price, ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)))
                .setText(R.id.tv_rate, MarketUtil.getMarketRateValue(rateType, upDownRate))
                .setTextColor(R.id.tv_rate,  ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)))
                .setText(R.id.tv_volume, MarketUtil.getVolumeValue(String.valueOf(item.getTurnVolume()), false));
    }
}
