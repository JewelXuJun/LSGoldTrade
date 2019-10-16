package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HoldPositionsAdapter extends BaseQuickAdapter<PositionVo, BaseViewHolder> {

    private Context mContext;

    private List<String> mList;
    private List<FiveSpeedVo> mFiveSpeedVos;

    public HoldPositionsAdapter(Context context, @Nullable List<PositionVo> data) {
        super(R.layout.item_hold_positions, data);

        mContext = context;
        mList = new ArrayList<>();
    }

    public void setList(List<String> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public void setFiveSpeedVoList(List<FiveSpeedVo> fiveSpeedVoList) {
        mFiveSpeedVos = fiveSpeedVoList;

        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionVo item) {
        if (null == item)
            return;

        String type = item.getType();
        String contractID = item.getContractId();
        String floatValue = "";
        String lastPrice = "";
        String upDown = "";
        String average = item.getPositionAverageStr();
        long position = item.getPosition();
        long frozen = item.getOffsetFrozen();

        if (null != mList && 0 != mList.size() && mList.size() > helper.getAdapterPosition())
            floatValue = mList.get(helper.getAdapterPosition());

        if (null != mFiveSpeedVos) {
            for (FiveSpeedVo fiveSpeedVo : mFiveSpeedVos) {
                if (null != fiveSpeedVo && fiveSpeedVo.getContractId().equals(contractID)) {
                    lastPrice = fiveSpeedVo.getLatestPriceValue();
                    upDown = fiveSpeedVo.getUpDownValue();
                }
            }
        }

        helper.setText(R.id.tv_pupil, type)
                .setTextColor(R.id.tv_pupil, type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_contract, contractID)
                .setText(R.id.tv_profit_loss, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatFloating(floatValue))
                .setTextColor(R.id.tv_profit_loss, TextUtils.isEmpty(floatValue) ? ContextCompat.getColor(mContext, R.color.color_text_normal)
                        : ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(new BigDecimal(floatValue).compareTo(new BigDecimal(0)))))
                .setText(R.id.tv_last_price, lastPrice)
                .setTextColor(R.id.tv_last_price, TextUtils.isEmpty(upDown) ? ContextCompat.getColor(mContext, R.color.color_text_normal)
                        : ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(new BigDecimal(upDown).compareTo(new BigDecimal(0)))))
                .setText(R.id.tv_average_price, MarketUtil.decimalFormatMoney(average))
                .setText(R.id.tv_amount, String.valueOf(position))
                .setText(R.id.tv_amount_available, String.valueOf(position - frozen))
                .addOnClickListener(R.id.layout_stop_transaction)
                .addOnClickListener(R.id.btn_evening_up);
    }
}
