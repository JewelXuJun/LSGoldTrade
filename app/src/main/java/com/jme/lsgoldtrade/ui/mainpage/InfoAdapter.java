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

        int position = helper.getAdapterPosition();
        String sendTime = item.getSendTime();

        if (0 == position)
            helper.setBackgroundRes(R.id.img, R.mipmap.ic_info1);
        else if (1 == position)
            helper.setBackgroundRes(R.id.img, R.mipmap.ic_info2);
        else if (2 == position)
            helper.setBackgroundRes(R.id.img, R.mipmap.ic_info3);
        else if (3 == position)
            helper.setBackgroundRes(R.id.img, R.mipmap.ic_info4);
        else if (4 == position)
            helper.setBackgroundRes(R.id.img, R.mipmap.ic_info5);

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, sendTime/*TextUtils.isEmpty(sendTime) ? "" : DateUtil.dateToString(DateUtil.dateToLong(sendTime))*/);
    }
}
