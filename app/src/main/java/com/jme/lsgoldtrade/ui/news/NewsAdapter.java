package com.jme.lsgoldtrade.ui.news;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NoticeVo;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<NoticeVo.NoticeBean, BaseViewHolder> {

    public NewsAdapter(int layoutResId, @Nullable List<NoticeVo.NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeVo.NoticeBean item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, item.getCreateTime())
                .setText(R.id.tv_content, item.getContent());
    }
}
