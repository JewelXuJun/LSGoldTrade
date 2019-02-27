package com.jme.lsgoldtrade.ui.mainpage;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfoAdapter extends BaseQuickAdapter<InfoVo.InfoBean, BaseViewHolder> {

    public InfoAdapter(int layoutResId, @Nullable List<InfoVo.InfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InfoVo.InfoBean item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, item.getCreateTime());

        Picasso.with(mContext)
                .load(item.getTitleImg())
                .placeholder(R.mipmap.ic_img_default)
                .error(R.mipmap.ic_img_default)
                .into((ImageView) helper.getView(R.id.img));
    }
}
