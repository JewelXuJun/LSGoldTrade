package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestionVo;

import java.util.List;

public class QuestionAboutAdapter extends BaseQuickAdapter<QuestionVo, BaseViewHolder> {

    private Context mContext;

    public QuestionAboutAdapter(Context context, int resId, List<QuestionVo> data) {
        super(resId, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionVo item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_question, item.getTitle())
                .setText(R.id.tv_answer, item.getAnwser());

        helper.getView(R.id.img_down).setOnClickListener((view) -> {
            helper.setTextColor(R.id.tv_question, ContextCompat.getColor(mContext, R.color.color_blue_deep))
                    .setGone(R.id.tv_answer, true)
                    .setGone(R.id.img_down, false)
                    .setGone(R.id.img_up, true);
        });

        helper.getView(R.id.img_up).setOnClickListener((view) -> {
            helper.setTextColor(R.id.tv_question, ContextCompat.getColor(mContext, R.color.black))
                    .setGone(R.id.tv_answer, false)
                    .setGone(R.id.img_down, true)
                    .setGone(R.id.img_up, false);
        });
    }
}
