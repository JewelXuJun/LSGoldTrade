package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DeclarationFormHoldPositionAdapter extends BaseQuickAdapter<PositionVo, BaseViewHolder> {

    private Context mContext;
    private List<String> mList;

    public DeclarationFormHoldPositionAdapter(Context context, int layoutResId, @Nullable List<PositionVo> data) {
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

        helper.setGone(R.id.layout_item, position == 0 ? true : true)
                .setText(R.id.tv_contract, contractID)
                .setText(R.id.tv_pupil, type)
                .setTextColor(R.id.tv_pupil, type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_available, String.valueOf(position - frozen))
                .setText(R.id.tv_position, String.valueOf(position))
                .setText(R.id.tv_average_price, MarketUtil.decimalFormatMoney(average))
                .setText(R.id.tv_profit_loss, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(floatValue))
                .setTextColor(R.id.tv_profit_loss, ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)))
                .addOnClickListener(R.id.btn_evening_up);
    }

}
