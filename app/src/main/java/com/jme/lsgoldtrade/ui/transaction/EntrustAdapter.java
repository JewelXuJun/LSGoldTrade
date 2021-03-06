package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
        int status = item.getStatus();

        if (mType.equals("History"))
            helper.setGone(R.id.tv_date, mList.get(helper.getAdapterPosition()));
        else
            helper.setGone(R.id.tv_date, false);

        helper.setText(R.id.tv_date, date)
                .setText(R.id.tv_contract, item.getContractId())
                .setText(R.id.tv_time, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"))
                .setText(R.id.tv_type, MarketUtil.getTradeDirection(bsFlag) + MarketUtil.getOCState(item.getOcFlag()))
                .setTextColor(R.id.tv_type, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_price, "-0.01".equals(MarketUtil.decimalFormatMoney(item.getMatchPriceStr())) ? "市价FAK" : item.getMatchPriceStr())
                .setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, MarketUtil.getTradeDirectionColor(bsFlag)))
                .setText(R.id.tv_entrust, String.valueOf(item.getEntrustNumber()))
                .setText(R.id.tv_surplus, String.valueOf(item.getRemnantNumber()))
                .setText(R.id.tv_state, MarketUtil.getEntrustState(status))
                .setGone(R.id.tv_state, status == 3 || status == 4 ? false : true)
                .setGone(R.id.btn_cancel_order, status == 3 || status == 4 ? true : false)
                .addOnClickListener(R.id.btn_cancel_order);
    }
}
