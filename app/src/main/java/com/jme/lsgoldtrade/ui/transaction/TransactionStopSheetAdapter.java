package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionStopSheetAdapter extends BaseQuickAdapter<ConditionOrderInfoVo, BaseViewHolder> {

    private Context mContext;

    private List<Boolean> mList;

    public TransactionStopSheetAdapter(Context context, @Nullable List<ConditionOrderInfoVo> data) {
        super(R.layout.item_transaction_stop_sheet, data);

        mContext = context;
        mList = new ArrayList<>();
    }

    public void setList(List<Boolean> list) {
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConditionOrderInfoVo item) {
        if (null == item)
            return;

        String contractId = item.getContractId();
        String stime = item.getStime();
        long stopProfitPrice = item.getStopProfitPrice();
        long stopLossPrice = item.getStopLossPrice();
        int bsFlag = item.getBsFlag();
        int ocFlag = item.getOcFlag();
        int status = item.getStatus();
        int effectiveTimeFlag = item.getEffectiveTimeFlag();
        int color = ContextCompat.getColor(mContext, MarketUtil.getEffectiveStateColor(status));

        helper.setGone(R.id.tv_date, null == mList || 0 == mList.size() || helper.getAdapterPosition() >= mList.size()
                ? false : mList.get(helper.getAdapterPosition()))
                .setText(R.id.tv_date, item.getSetDate())
                .setText(R.id.tv_contract, TextUtils.isEmpty(contractId) ? mContext.getResources().getString(R.string.text_no_data_default) : contractId)
                .setText(R.id.tv_time, TextUtils.isEmpty(stime) ? mContext.getResources().getString(R.string.text_no_data_default) : stime)
                .setText(R.id.tv_type, bsFlag == 1 && ocFlag == 1 ? mContext.getResources().getString(R.string.market_equal_empty)
                        : bsFlag == 2 && ocFlag == 1 ? mContext.getResources().getString(R.string.market_equal_more) : "")
                .setText(R.id.tv_amount, String.valueOf(item.getEntrustNumber()))
                .setText(R.id.tv_stop_profit_price, getStopProfitPriceValue(stopProfitPrice, bsFlag, ocFlag))
                .setText(R.id.tv_stop_loss_price, getStopLossPriceValue(stopLossPrice, bsFlag, ocFlag))
                .setText(R.id.tv_state, MarketUtil.getTransactionState(status))
                .setText(R.id.tv_effective_type, getEffectiveTimeType(effectiveTimeFlag))
                .setGone(R.id.btn_cancel, status == 0)
                .setTextColor(R.id.tv_contract, color)
                .setTextColor(R.id.tv_type, color)
                .setTextColor(R.id.tv_amount, color)
                .setTextColor(R.id.tv_stop_profit_price, color)
                .setTextColor(R.id.tv_stop_loss_price, color)
                .setTextColor(R.id.tv_state, ContextCompat.getColor(mContext, MarketUtil.getSheetStateColor(status)))
                .addOnClickListener(R.id.btn_cancel);

    }

    private String getStopProfitPriceValue(long stopProfitPrice, int bsFlag, int ocFlag) {
        String value;

        if (1 == stopProfitPrice) {
            value = mContext.getResources().getString(R.string.transaction_not_setting);
        } else {
            if (bsFlag == 1 && ocFlag == 1)
                value = "<=" + MarketUtil.getPriceValue(stopProfitPrice);
            else if (bsFlag == 2 && ocFlag == 1)
                value = ">=" + MarketUtil.getPriceValue(stopProfitPrice);
            else
                value = "";
        }

        return String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price), value);
    }

    private String getStopLossPriceValue(long stopLossPrice, int bsFlag, int ocFlag) {
        String value;

        if (1 == stopLossPrice) {
            value = mContext.getResources().getString(R.string.transaction_not_setting);
        } else {
            if (bsFlag == 1 && ocFlag == 1)
                value = ">=" + MarketUtil.getPriceValue(stopLossPrice);
            else if (bsFlag == 2 && ocFlag == 1)
                value = "<=" + MarketUtil.getPriceValue(stopLossPrice);
            else
                value = "";
        }

        return String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price), value);
    }

    private String getEffectiveTimeType(int effectiveTimeFlag) {
        String value = "";

        if (effectiveTimeFlag == 0)
            value = mContext.getResources().getString(R.string.transaction_effective_on_that_day);
        else if (effectiveTimeFlag == 1)
            value = mContext.getResources().getString(R.string.transaction_effective_before_cancel);

        return value;
    }

}
