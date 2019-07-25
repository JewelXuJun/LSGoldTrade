package com.jme.lsgoldtrade.ui.personal;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.UserDetailsVo;
import java.math.BigDecimal;
import java.util.List;

public class DetailsAdapter extends BaseQuickAdapter<UserDetailsVo, BaseViewHolder> {

    public DetailsAdapter(List<UserDetailsVo> data) {
        super(R.layout.item_user_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDetailsVo item) {
        String type = item.getType();
        if (type.equals("当日服务费"))
            type += "\n(当日17点后更新)";
        helper.setText(R.id.tv_type, type);
        helper.setText(R.id.tv_status, item.getStatus());
        helper.setText(R.id.tv_funds, BigDecimalUtil.formatMoney(new BigDecimal(item.getAmount()).divide(new BigDecimal(100)).toPlainString()));
    }
}

