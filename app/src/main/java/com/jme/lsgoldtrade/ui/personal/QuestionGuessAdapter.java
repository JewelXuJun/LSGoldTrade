package com.jme.lsgoldtrade.ui.personal;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestionGuessVo;

import java.util.List;

public class QuestionGuessAdapter extends BaseQuickAdapter<QuestionGuessVo.QuestionBean, BaseViewHolder> {

    public QuestionGuessAdapter(int resId, List<QuestionGuessVo.QuestionBean> data) {
        super(resId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionGuessVo.QuestionBean item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_question, helper.getAdapterPosition() + 1 + "." + item.getTitle());
    }
}
