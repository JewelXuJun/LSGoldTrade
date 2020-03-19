package com.jme.lsgoldtrade.ui.personal;

import android.widget.TextView;

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

        String bankName = item.getBankName();

        helper.setText(R.id.tv_bank_name, bankName)
                .setText(R.id.tv_bankcard, StringUtils.formatBankCardDefault(item.getBankNo()));

        ((TextView)helper.getView(R.id.tv_bank_name)).setTextSize(bankName.length() > 4 ? 14 : 15);
        ((TextView)helper.getView(R.id.tv_bankcard)).setTextSize(bankName.length() > 4 ? 13 : 15);
    }
}

