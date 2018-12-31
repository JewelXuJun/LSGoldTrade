package com.jme.lsgoldtrade.ui.mainpage;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NoticePageVo;

import java.util.List;

public class InfoAdapter extends BaseQuickAdapter<NoticePageVo.NoticeBean, BaseViewHolder> {

    public InfoAdapter(int layoutResId, @Nullable List<NoticePageVo.NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticePageVo.NoticeBean item) {
        if (null == item)
            return;

        String sendTime = item.getSendTime();

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, TextUtils.isEmpty(sendTime) ? "" : DateUtil.dateToString(DateUtil.dateToLong(sendTime)));
    }
}
