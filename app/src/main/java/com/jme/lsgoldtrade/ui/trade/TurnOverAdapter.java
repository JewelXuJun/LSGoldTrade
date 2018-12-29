package com.jme.lsgoldtrade.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class TurnOverAdapter extends BaseQuickAdapter<InOutTurnOverVo.TurnOverBean, BaseViewHolder> {

    public TurnOverAdapter(int layoutResId, @Nullable List<InOutTurnOverVo.TurnOverBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InOutTurnOverVo.TurnOverBean item) {
        if (null == item)
            return;

        String date = item.getTradeDate();
        String direction = item.getDirection();

        if (!TextUtils.isEmpty(date) && date.contains("-"))
            date.replace("-", "/");

        helper.setText(R.id.tv_time, date)
                .setText(R.id.tv_amount, MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(item.getAmount())))
                .setTextColor(R.id.tv_amount, MarketUtil.getInOutMoneyDirectionColor(direction))
                .setText(R.id.tv_type, MarketUtil.getInOutMoneyDirection(direction))
                .setTextColor(R.id.tv_type, MarketUtil.getInOutMoneyDirectionColor(direction))
                .setText(R.id.tv_state, MarketUtil.getInOutMoneyState(item.getDepositFlag()));
    }


}
