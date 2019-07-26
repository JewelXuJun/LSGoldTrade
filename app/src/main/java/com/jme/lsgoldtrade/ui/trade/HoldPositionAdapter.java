package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.HangQingWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HoldPositionAdapter extends BaseQuickAdapter<PositionVo, BaseViewHolder> {

    private Context mContext;
    private List<String> mList;
    private int mPosition = -1;
    private int mType;

    public HoldPositionAdapter(Context context, int layoutResId, @Nullable List<PositionVo> data, int type) {
        super(layoutResId, data);

        mContext = context;
        mList = new ArrayList<>();
        mType = type;
    }

    public void setList(List<String> list) {
        mList = list;
    }

    public void setSelectPosition(int position) {
        mPosition = position;
    }

    public int getSelectPosition() {
        return mPosition;
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionVo item) {
        if (null == item)
            return;

        boolean isSelected = mPosition == helper.getAdapterPosition();
        String floatValue = "";
        int typeValue = 0;

        String contractID = item.getContractId();
        String type = item.getType();
        if (null != mList && 0 != mList.size() && mList.size() > helper.getAdapterPosition())
            floatValue = mList.get(helper.getAdapterPosition());
        String average = item.getPositionAverageStr();
        long position = item.getPosition();
        helper.getView(R.id.layout_item).setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        long frozen = item.getOffsetFrozen();
        if (!TextUtils.isEmpty(floatValue))
            typeValue = new BigDecimal(floatValue).compareTo(new BigDecimal(0));

        long floatProfit = item.getFloatProfit();

        helper.setBackgroundColor(R.id.layout_item, isSelected ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.white))
                .setText(R.id.tv_contract, contractID)
                .setTextColor(R.id.tv_contract, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_pupil, type)
                .setTextColor(R.id.tv_pupil, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_profit_loss, (floatProfit / 100) + "")
                .setTextColor(R.id.tv_profit_loss, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : type.equals("多") ? ContextCompat.getColor(mContext, R.color.common_font_increase)
                        : ContextCompat.getColor(mContext, R.color.common_font_decrease))
                .setText(R.id.tv_available, String.valueOf(position - frozen))
                .setTextColor(R.id.tv_available, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_position, String.valueOf(position))
                .setTextColor(R.id.tv_position, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : ContextCompat.getColor(mContext, R.color.color_text_black))
                .setText(R.id.tv_average_price, MarketUtil.decimalFormatMoney(average))
                .setTextColor(R.id.tv_average_price, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                        : ContextCompat.getColor(mContext, R.color.color_text_black));

        if (mType == 1) {
            helper.getView(R.id.tv_rate).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_profit_loss).setVisibility(View.GONE);
            helper.setText(R.id.tv_float, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(floatValue))
                    .setTextColor(R.id.tv_float, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                            : ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)))
                    .setText(R.id.tv_rate, TextUtils.isEmpty(floatValue) ? mContext.getResources().getString(R.string.text_no_data_default) : getRate(contractID, floatValue, average, position))
                    .setTextColor(R.id.tv_rate, isSelected ? ContextCompat.getColor(mContext, R.color.white)
                            : ContextCompat.getColor(mContext, MarketUtil.getPriceStateColor(typeValue)));
        } else {
            helper.getView(R.id.tv_profit_loss).setVisibility(View.VISIBLE);
            TextView tv_float = helper.getView(R.id.tv_float);
            helper.getView(R.id.tv_rate).setVisibility(View.GONE);
            tv_float.setText("平仓");
            tv_float.setTextColor(mContext.getResources().getColor(R.color.white));
            tv_float.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_blue_solid));
            tv_float.setPadding(DensityUtil.dpTopx(mContext, 5), DensityUtil.dpTopx(mContext, 5), DensityUtil.dpTopx(mContext, 5), DensityUtil.dpTopx(mContext, 5));
            tv_float.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HangQingWindow mWindow = new HangQingWindow(mContext);
                    mWindow.setOutsideTouchable(true);
                    mWindow.setFocusable(true);
                    if (listener != null) {
                        listener.listener(item, mWindow);
                    }
                }
            });
        }
    }

    public setOnPingCangListener listener;

    public interface setOnPingCangListener {
        void listener(PositionVo item, HangQingWindow mWindow);
    }

    public void setOnPingCangListener(setOnPingCangListener listener) {
        this.listener = listener;
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
