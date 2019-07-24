package com.jme.lsgoldtrade.ui.tradingbox;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.TradingBoxOrderVo;

import java.util.List;

public class TradingBoxOrderAdapter extends BaseQuickAdapter<TradingBoxOrderVo, BaseViewHolder> {

    public TradingBoxOrderAdapter(int resId, @Nullable List<TradingBoxOrderVo> data) {
        super(resId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingBoxOrderVo item) {
        if (null == item)
            return;

        String direction = item.getEntrustTheDirection();
        String createTime = item.getCratedTime();
        String flag = item.getFlag();
        String status = item.getStatus();

        helper.setText(R.id.tv_contract, item.getOrders())
                .setText(R.id.tv_direction, direction.equals("0") ? mContext.getResources().getString(R.string.text_more)
                        : mContext.getResources().getString(R.string.text_empty))
                .setTextColor(R.id.tv_direction, direction.equals("0") ? ContextCompat.getColor(mContext, R.color.color_red)
                        : ContextCompat.getColor(mContext, R.color.color_green))
                .setText(R.id.tv_number, item.getEntrustTheHandCount())
                .setText(R.id.tv_time, TextUtils.isEmpty(createTime) ? "" : createTime.replace("-", "/"))
                .setText(R.id.tv_status, getStatus(status))
                .setGone(R.id.btn_cancel, flag.equals("false") ? false
                        : status.equals("0") || status.equals("1") || status.equals("2") || status.equals("3") ? true : false)
                .addOnClickListener(R.id.btn_cancel)
                .addOnClickListener(R.id.btn_detail);
    }

    private String getStatus(String status) {
        String value = "";

        if (status.equals("0"))
            value = "委托中";
        else if (status.equals("1"))
            value = "建仓";
        else if (status.equals("2"))
            value = "建仓中";
        else if (status.equals("3"))
            value = "平仓";
        else if (status.equals("4"))
            value = "平仓中";
        else if (status.equals("5"))
            value = "委托完成";
        else if (status.equals("6"))
            value = "撤销中";
        else if (status.equals("7"))
            value = "已撤销";
        else if (status.equals("8"))
            value = "建仓完成";

        return value;
    }

}
