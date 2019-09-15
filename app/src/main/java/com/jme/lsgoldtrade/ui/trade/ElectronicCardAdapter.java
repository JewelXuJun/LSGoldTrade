package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.TransactionDetailVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class ElectronicCardAdapter extends BaseQuickAdapter<TransactionDetailVo.RecordsBean, BaseViewHolder> {

    private Context mContext;

    public ElectronicCardAdapter(Context context, int layoutResId, @Nullable List<TransactionDetailVo.RecordsBean> data) {
        super(layoutResId, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TransactionDetailVo.RecordsBean item) {
        if (null == item)
            return;

       String time[] = item.getCreatedTime().split(" ");
       String businessStatus = item.getBusinessStatus();

        helper.setText(R.id.tv_time, time[0])
                .setText(R.id.trade_time, time[1])
                .setText(R.id.tv_amount, MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(item.getAmount())))
                .setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, MarketUtil.getInOutMoneyStatusColor(businessStatus)))
                .setText(R.id.tv_type, TextUtils.isEmpty(businessStatus)
                        ? "" : businessStatus.equals("recharge") ? mContext.getResources().getString(R.string.trade_transfer_icbc_electronic_card_in)
                        : businessStatus.equals("withdraw") ? mContext.getResources().getString(R.string.trade_transfer_icbc_electronic_card_out) : "")
                .setTextColor(R.id.tv_type, ContextCompat.getColor(mContext, MarketUtil.getInOutMoneyStatusColor(businessStatus)))
                .setText(R.id.tv_state, getState(item.getStatus()));
    }

    private String getState(String status) {
        String state;

        if (TextUtils.isEmpty(status)) {
            state = "";
        } else {
            if (status.equals("true"))
                state = mContext.getResources().getString(R.string.trade_transfer_success);
            else if (status.equals("false"))
                state = mContext.getResources().getString(R.string.trade_transfer_fail);
            else if (status.equals("processing"))
                state = mContext.getResources().getString(R.string.trade_transfer_processing);
            else
                state = "";
        }

        return state;
    }

}
