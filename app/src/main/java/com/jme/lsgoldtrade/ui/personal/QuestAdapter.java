package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestVo;

import java.util.List;

public class QuestAdapter extends BaseQuickAdapter<QuestVo.QuestionListBean, BaseViewHolder> {

    public QuestAdapter(Context mContext, int resId, List<QuestVo.QuestionListBean> data) {
        super(resId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestVo.QuestionListBean item) {
        helper.setText(R.id.quest, item.getTitle());
    }
}
