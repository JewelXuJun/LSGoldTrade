package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class CancelOrderAdapter extends BaseQuickAdapter<OrderPageVo.OrderBean, BaseViewHolder> {

    private Context mContext;
    private int mPosition = -1;

    public CancelOrderAdapter(Context context, int layoutResId, @Nullable List<OrderPageVo.OrderBean> data) {
        super(layoutResId, data);

        mContext = context;
    }

    public void setSelectPosition(int position) {
        mPosition = position;
    }

    public int getSelectPosition() {
        return mPosition;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderPageVo.OrderBean item) {
        if (null == item)
            return;

        boolean isSelected = mPosition == helper.getAdapterPosition();

        String time = item.getDeclareTime();
        int bsFlag = item.getBsFlag();

        helper.setBackgroundColor(R.id.layout_item, isSelected ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.white))
                .setText(R.id.tv_contract, item.getContractId())
                .setTextColor(R.id.tv_contract, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_time, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"))
                .setTextColor(R.id.tv_time, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, R.color.color_text_gray_hint))
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setTextColor(R.id.tv_price, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_entrust, String.valueOf(item.getEntrustNumber()))
                .setTextColor(R.id.tv_entrust, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_surplus, String.valueOf(item.getRemnantNumber()))
                .setTextColor(R.id.tv_surplus, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_state, MarketUtil.getEntrustState(item.getStatus()))
                .setTextColor(R.id.tv_state, isSelected ? ContextCompat.getColor(mContext, R.color.white) :
                        ContextCompat.getColor(mContext, R.color.color_text_black));
    }
}
