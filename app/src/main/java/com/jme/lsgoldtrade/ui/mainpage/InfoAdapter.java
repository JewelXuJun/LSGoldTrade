package com.jme.lsgoldtrade.ui.mainpage;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.view.CollapsibleTextView;
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

        if (TextUtils.isEmpty(item.getTitleImg())) {
            helper.getView(R.id.nocelve).setVisibility(View.GONE);
            helper.getView(R.id.celve).setVisibility(View.VISIBLE);
            CollapsibleTextView celvecontent = helper.getView(R.id.celvecontent);
            celvecontent.setDesc(item.getTitle(), TextView.BufferType.NORMAL);
            helper.setText(R.id.celvetime, item.getCreateTime());
        } else {
            helper.getView(R.id.nocelve).setVisibility(View.VISIBLE);
            helper.getView(R.id.celve).setVisibility(View.GONE);
            helper.setText(R.id.tv_title, item.getTitle())
                    .setText(R.id.tv_time, item.getCreateTime());

            Picasso.with(mContext)
                    .load(item.getTitleImg())
                    .placeholder(R.mipmap.ic_img_default)
                    .error(R.mipmap.ic_img_default)
                    .into((ImageView) helper.getView(R.id.img));
        }
    }
}
