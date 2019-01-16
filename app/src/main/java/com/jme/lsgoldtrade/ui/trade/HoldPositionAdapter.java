package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HoldPositionAdapter extends BaseQuickAdapter<PositionVo, BaseViewHolder> {

    private Context mContext;

    private List<String> mList;

    public HoldPositionAdapter(Context context, int layoutResId, @Nullable List<PositionVo> data) {
        super(layoutResId, data);

        mContext = context;
        mList = new ArrayList<>();
    }

    public void setList(List<String> list) {
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionVo item) {
        if (null == item)
            return;

        String type = item.getType();
        String floatValue = mList.get(helper.getAdapterPosition());
        String average = item.getPositionAverageStr();
        long position = item.getPosition();
        long frozen = item.getOffsetFrozen();
        int typeValue = new BigDecimal(floatValue).compareTo(new BigDecimal(0));

        helper.setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_pupil, type)
                .setTextColor(R.id.tv_pupil, type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_available, String.valueOf(position - frozen))
                .setText(R.id.tv_position, String.valueOf(position))
                .setText(R.id.tv_average_price, MarketUtil.decimalFormatMoney(average))
                .setText(R.id.tv_float, MarketUtil.decimalFormatMoney(floatValue))
                .setTextColor(R.id.tv_float, ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)))
                .setText(R.id.tv_rate, getRate(floatValue, average, position))
                .setTextColor(R.id.tv_rate, ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)));
    }

    private String getRate(String floatStr, String average, long position) {
        String rateStr;

        BigDecimal floatValue =  new BigDecimal(floatStr);
        BigDecimal positionValue = new BigDecimal(average).multiply(new BigDecimal(position));

        if (positionValue.compareTo(new BigDecimal(0)) == 0) {
            if (floatValue.compareTo(new BigDecimal(0)) == 1)
                rateStr = "100.00%";
            else if (floatValue.compareTo(new BigDecimal(0)) == 0)
                rateStr = "0.00%";
            else
                rateStr = "-100.00%";
        } else {
            rateStr = floatValue.divide(positionValue, 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).setScale(2).toPlainString() + "%";
        }

        return rateStr;
    }

}
