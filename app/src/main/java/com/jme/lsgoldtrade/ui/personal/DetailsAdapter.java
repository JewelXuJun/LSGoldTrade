package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.bean.UserDetailsVo;

import java.util.List;

public class DetailsAdapter extends BaseQuickAdapter<UserDetailsVo, BaseViewHolder> {

    public DetailsAdapter(int layoutResId, List<UserDetailsVo> data, Context context) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDetailsVo item) {
        helper.setText(R.id.cashtype, item.getType());
        helper.setText(R.id.cashstatus, item.getStatus());
        helper.setText(R.id.cashmoney, item.getAmount());
    }
}

