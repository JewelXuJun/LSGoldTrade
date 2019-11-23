package com.jme.lsgoldtrade.ui.personal;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.BankVo;

import java.util.List;

public class BankCardAdapter extends BaseQuickAdapter<BankVo, BaseViewHolder> {

    public BankCardAdapter(List<BankVo> data) {
        super(R.layout.item_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankVo item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_bank_name, item.getBankName())
                .setText(R.id.tv_bankcard, StringUtils.formatBankCardDefault(item.getBankNo()));
    }
}

