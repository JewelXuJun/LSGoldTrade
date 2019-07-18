package com.jme.lsgoldtrade.ui.mainpage;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.QuestListVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AboutQuestAdapter extends BaseQuickAdapter<QuestListVo, BaseViewHolder> {

    public AboutQuestAdapter(int resId, List<QuestListVo> date) {
        super(resId, date);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestListVo item) {
        helper.setText(R.id.tab, item.getName());
        Picasso.with(mContext)
                .load(item.getPic())
                .placeholder(R.mipmap.ic_img_default)
                .error(R.mipmap.ic_img_default)
                .into((ImageView) helper.getView(R.id.image));
    }
}
