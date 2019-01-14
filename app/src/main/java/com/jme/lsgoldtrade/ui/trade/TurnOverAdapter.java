package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class TurnOverAdapter extends BaseQuickAdapter<InOutTurnOverVo.TurnOverBean, BaseViewHolder> {

    private Context mContext;

    public TurnOverAdapter(Context context, int layoutResId, @Nullable List<InOutTurnOverVo.TurnOverBean> data) {
        super(layoutResId, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InOutTurnOverVo.TurnOverBean item) {
        if (null == item)
            return;

        String date = item.getTradeDate();
        int direction = item.getDirection();

        if (!TextUtils.isEmpty(date) && date.contains("-"))
            date = date.replace("-", "/");

        helper.setText(R.id.tv_time, date)
                .setText(R.id.tv_amount, MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(item.getAmount())))
                .setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, MarketUtil.getInOutMoneyDirectionColor(direction)))
                .setText(R.id.tv_type, item.getSummary())
                .setTextColor(R.id.tv_type, ContextCompat.getColor(mContext, MarketUtil.getInOutMoneyDirectionColor(direction)))
                .setText(R.id.tv_state, MarketUtil.getInOutMoneyState(item.getDepositFlag()));
    }

}
