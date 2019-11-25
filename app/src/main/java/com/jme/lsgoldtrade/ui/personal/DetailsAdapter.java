package com.jme.lsgoldtrade.ui.personal;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.UserDetailsVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.List;

public class DetailsAdapter extends BaseQuickAdapter<UserDetailsVo, BaseViewHolder> {

    public DetailsAdapter(List<UserDetailsVo> data) {
        super(R.layout.item_user_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDetailsVo item) {
        if (null == item)
            return;

        String operateTime = item.getOperateTime();

        if (TextUtils.isEmpty(operateTime)) {
            helper.setText(R.id.tv_date, "")
                    .setGone(R.id.tv_time, false);
        } else {
            String[] timeValue = item.getOperateTime().split(" ");

            helper.setText(R.id.tv_date, timeValue[0]);

            if (timeValue.length == 2) {
                helper.setText(R.id.tv_time, timeValue[1])
                        .setGone(R.id.tv_time, true);
            } else {
                helper.setGone(R.id.tv_time, false);
            }
        }

        helper.setText(R.id.tv_money, BigDecimalUtil.formatMoney(new BigDecimal(item.getAmount()).divide(new BigDecimal(100)).toPlainString()))
                .setText(R.id.tv_type, item.getOperateType());
    }
}

