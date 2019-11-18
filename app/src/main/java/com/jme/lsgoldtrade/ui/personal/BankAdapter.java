package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;

import java.util.List;

public class BankAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BankAdapter(Context context, List<String> data) {
        super(R.layout.item_bank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (null == item)
            return;

    }

}
