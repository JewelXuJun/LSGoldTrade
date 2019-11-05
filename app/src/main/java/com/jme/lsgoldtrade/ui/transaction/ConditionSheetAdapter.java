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

public class ConditionSheetAdapter extends BaseQuickAdapter<ConditionOrderInfoVo, BaseViewHolder> {

    private Context mContext;

    private List<Boolean> mList;

    public ConditionSheetAdapter(Context context, @Nullable List<ConditionOrderInfoVo> data) {
        super(R.layout.item_condition_sheet, data);

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
        String triggerPrice = item.getTriggerPriceStr();
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
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(ocFlag))
                .setText(R.id.tv_amount, String.valueOf(item.getEntrustNumber()))
                .setText(R.id.tv_trigger_price, getTriggerPriceValue(triggerPrice, bsFlag, ocFlag))
                .setText(R.id.tv_trigger_price_type, mContext.getResources().getString(R.string.transaction_market_price_fak))
                .setText(R.id.tv_state, MarketUtil.getTransactionState(status))
                .setText(R.id.tv_effective_type, getEffectiveTimeType(effectiveTimeFlag))
                .setGone(R.id.btn_modify, status == 0)
                .setGone(R.id.btn_cancel, status == 0)
                .setTextColor(R.id.tv_contract, color)
                .setTextColor(R.id.tv_type, color)
                .setTextColor(R.id.tv_amount, color)
                .setTextColor(R.id.tv_trigger_price, color)
                .setTextColor(R.id.tv_trigger_price_type, color)
                .setTextColor(R.id.tv_state, ContextCompat.getColor(mContext, MarketUtil.getSheetStateColor(status)))
                .addOnClickListener(R.id.btn_modify)
                .addOnClickListener(R.id.btn_cancel);

    }

    private String getTriggerPriceValue(String triggerPrice, int bsFlag, int ocFlag) {
        String value;

        if (TextUtils.isEmpty(triggerPrice)) {
            value = "";
        } else {
            if (bsFlag == 1 && ocFlag == 0) {
                value = String.format(mContext.getResources().getString(R.string.transaction_last_price_buy_more), triggerPrice);
            } else if (bsFlag == 2 && ocFlag == 0) {
                value = String.format(mContext.getResources().getString(R.string.transaction_last_price_sell_empty), triggerPrice);
            } else {
                value = "";
            }
        }

        return value;
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
