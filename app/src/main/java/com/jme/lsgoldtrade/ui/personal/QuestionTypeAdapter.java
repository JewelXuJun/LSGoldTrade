package com.jme.lsgoldtrade.ui.personal;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestListTypeVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuestionTypeAdapter extends BaseQuickAdapter<QuestListTypeVo, BaseViewHolder> {

    public QuestionTypeAdapter(int resId, List<QuestListTypeVo> date) {
        super(resId, date);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestListTypeVo item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_type, item.getName());

        Picasso.with(mContext)
                .load(item.getPic())
                .into((ImageView) helper.getView(R.id.img_type));
    }
}
