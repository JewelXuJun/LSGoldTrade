package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Contract;
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

        int typeValue = 0;
        String floatValue = "";
        String contractID = item.getContractId();
        String type = item.getType();
        String average = item.getPositionAverageStr();
        long position = item.getPosition();
        long frozen = item.getOffsetFrozen();

        if (null != mList && 0 != mList.size() && mList.size() > helper.getAdapterPosition())
            floatValue = mList.get(helper.getAdapterPosition());

        if (!TextUtils.isEmpty(floatValue))
            typeValue = new BigDecimal(floatValue).compareTo(new BigDecimal(0));

        helper.setGone(R.id.layout_item, position == 0 ? false : true)
                .setText(R.id.tv_contract, contractID)
                .setText(R.id.tv_pupil, type)
                .setTextColor(R.id.tv_pupil, type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_available, String.valueOf(position - frozen))
                .setText(R.id.tv_position, String.valueOf(position))
                .setText(R.id.tv_average_price, MarketUtil.decimalFormatMoney(average))
                .setText(R.id.tv_float, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(floatValue))
                .setTextColor(R.id.tv_float, ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)))
                .setText(R.id.tv_rate, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default) :
                        getRate(contractID, floatValue, average, position))
                .setTextColor(R.id.tv_rate, ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)));
    }

    private String getRate(String contractID, String floatStr, String average, long position) {
        String rateStr;

        long handWeight = Contract.getInstance().getHandWeightFromID(contractID);
        long contractValue = contractID.equals("Ag(T+D)") ? new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight;

        BigDecimal floatValue = new BigDecimal(floatStr);
        BigDecimal positionValue = new BigDecimal(average).multiply(new BigDecimal(contractValue))
                .multiply(new BigDecimal(position));

        if (positionValue.compareTo(new BigDecimal(0)) == 0) {
            if (floatValue.compareTo(new BigDecimal(0)) == 1)
                rateStr = "100.00%";
            else if (floatValue.compareTo(new BigDecimal(0)) == 0)
                rateStr = "0.00%";
            else
                rateStr = "-100.00%";
        } else {
            rateStr = floatValue.divide(positionValue, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2).toPlainString() + "%";
        }

        return rateStr;
    }

}
