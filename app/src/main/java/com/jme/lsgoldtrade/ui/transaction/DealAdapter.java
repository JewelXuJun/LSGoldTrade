package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends BaseQuickAdapter<DealPageVo.DealBean, BaseViewHolder> {

    private Context mContext;
    private String mType;
    private List<Boolean> mList;

    public DealAdapter(Context context, int layoutResId, @Nullable List<DealPageVo.DealBean> data, String type) {
        super(layoutResId, data);

        mContext = context;
        mType = type;
        mList = new ArrayList<>();
    }

    public void setList(List<Boolean> list) {
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, DealPageVo.DealBean item) {
        if (null == item)
            return;

        String date = item.getMatchDate();
        String time = item.getMatchTime();
        int bsFlag = item.getBsFlag();

        if (mType.equals("History"))
            helper.setGone(R.id.tv_date, mList.get(helper.getAdapterPosition()));
        else
            helper.setGone(R.id.tv_date, false);

        helper.setText(R.id.tv_date, date)
                .setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"))
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_amount, String.valueOf(item.getMatchHand()))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_turn_volume, MarketUtil.decimalFormatMoney(item.getAmountStr()));
    }
}
