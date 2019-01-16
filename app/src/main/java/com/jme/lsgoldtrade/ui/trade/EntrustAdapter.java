package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.ArrayList;
import java.util.List;

public class EntrustAdapter extends BaseQuickAdapter<OrderPageVo.OrderBean, BaseViewHolder> {

    private Context mContext;
    private String mType;
    private List<Boolean> mList;

    public EntrustAdapter(Context context, int layoutResId, @Nullable List<OrderPageVo.OrderBean> data, String type) {
        super(layoutResId, data);

        mContext = context;
        mType = type;

        mList = new ArrayList<>();
    }

    public void setList(List<Boolean> list) {
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderPageVo.OrderBean item) {
        if (null == item)
            return;

        String date = item.getTradeDate();
        String time = item.getDeclareTime();
        int bsFlag = item.getBsFlag();

        helper.setText(R.id.tv_date, date)
                .setGone(R.id.tv_date, mType.equals("History") && mList.get(helper.getAdapterPosition()))
                .setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"))
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_price, MarketUtil.decimalFormatMoney(item.getMatchPriceStr()))
                .setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_entrust, String.valueOf(item.getEntrustNumber()))
                .setText(R.id.tv_surplus, String.valueOf(item.getRemnantNumber()))
                .setText(R.id.tv_state, MarketUtil.getEntrustState(item.getStatus()));
    }
}
