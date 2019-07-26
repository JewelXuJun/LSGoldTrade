package com.jme.lsgoldtrade.ui.mainpage;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainPageFastItemAdapter extends BaseQuickAdapter<NavigatorVo.NavigatorVoBean, BaseViewHolder> {

    public MainPageFastItemAdapter(int layoutResId, @Nullable List<NavigatorVo.NavigatorVoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigatorVo.NavigatorVoBean item) {
        if (null == item)
            return;

        Picasso.with(mContext)
                .load(item.getImageName())
                .placeholder(R.mipmap.ic_img_default)
                .error(R.mipmap.ic_img_default)
                .into((ImageView) helper.getView(R.id.img));

        helper.setText(R.id.tv_tab_name, item.getName());

    }
}
