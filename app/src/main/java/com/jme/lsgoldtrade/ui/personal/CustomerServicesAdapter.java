package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestionAskVo;

import java.util.List;

public class CustomerServicesAdapter extends BaseQuickAdapter<QuestionAskVo, BaseViewHolder> {

    public CustomerServicesAdapter(Context context, int layoutResId, @Nullable List<QuestionAskVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionAskVo item) {
        if (null == item)
            return;
        helper.setText(R.id.quest, item.getAsk());
        helper.setText(R.id.ask, item.getQuestion());
    }
}
