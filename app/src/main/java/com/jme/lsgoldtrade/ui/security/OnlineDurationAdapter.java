package com.jme.lsgoldtrade.ui.security;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.OnlineTimeVo;

import java.util.List;

public class OnlineDurationAdapter extends BaseQuickAdapter<OnlineTimeVo, BaseViewHolder> {

    private long mCurrentTimeSeconds = 0;

    public OnlineDurationAdapter(@Nullable List<OnlineTimeVo> data) {
        super(R.layout.item_online_duration, data);
    }

    public void setCurrentTimeSeconds(long currentTimeSeconds) {
        mCurrentTimeSeconds = currentTimeSeconds;
    }

    public long getCurrentTimeSeconds() {
        return mCurrentTimeSeconds;
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineTimeVo item) {
        if (null == item)
            return;

        long timeSeconds = item.getTimeSeconds();

        helper.setText(R.id.tv_online_duration, item.getTimeName())
                .setGone(R.id.img_online_duration, 0 == mCurrentTimeSeconds || 0 == timeSeconds ? false
                        : mCurrentTimeSeconds == timeSeconds ? true : false);
    }
}
