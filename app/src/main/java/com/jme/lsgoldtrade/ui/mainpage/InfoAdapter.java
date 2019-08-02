package com.jme.lsgoldtrade.ui.mainpage;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfoAdapter extends BaseQuickAdapter<InfoVo.InfoBean, BaseViewHolder> {

    private long mChannelID;

    public InfoAdapter(int layoutResId, @Nullable List<InfoVo.InfoBean> data) {
        super(layoutResId, data);
    }

    public void setChannelID(long channelID) {
        mChannelID = channelID;
    }

    @Override
    protected void convert(BaseViewHolder helper, InfoVo.InfoBean item) {
        if (null == item)
            return;

        if (mChannelID == -10000) {
            helper.setText(R.id.tv_strategy_time, DateUtil.dateToStringWithAll(DateUtil.dateToLong(item.getCreateTime())))
                    .setText(R.id.tv_strategy, item.getTitle());

            TextView tv_strategy = helper.getView(R.id.tv_strategy);
            ImageView img_strategy_open = helper.getView(R.id.img_strategy_open);
            ImageView img_strategy_close = helper.getView(R.id.img_strategy_close);

            tv_strategy.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    tv_strategy.getViewTreeObserver().removeOnPreDrawListener(this);

                    int lineCount = tv_strategy.getLineCount();

                    if (lineCount > 2) {
                        img_strategy_open.setVisibility(View.VISIBLE);
                        img_strategy_close.setVisibility(View.GONE);
                        tv_strategy.setMaxLines(3);
                    } else {
                        img_strategy_open.setVisibility(View.GONE);
                        img_strategy_close.setVisibility(View.GONE);
                    }

                    return true;
                }
            });

            img_strategy_open.setOnClickListener((view) -> {
                tv_strategy.setSingleLine(false);
                img_strategy_open.setVisibility(View.GONE);
                img_strategy_close.setVisibility(View.VISIBLE);
            });

            img_strategy_close.setOnClickListener((view) -> {
                tv_strategy.setMaxLines(3);
                img_strategy_open.setVisibility(View.VISIBLE);
                img_strategy_close.setVisibility(View.GONE);
            });

        } else {
            Picasso.with(mContext)
                    .load(item.getTitleImg())
                    .placeholder(R.mipmap.ic_img_default)
                    .error(R.mipmap.ic_img_default)
                    .into((ImageView) helper.getView(R.id.img));

            helper.setText(R.id.tv_title, item.getTitle())
                    .setText(R.id.tv_time, item.getCreateTime())
                    .setGone(R.id.layout_info, true)
                    .setGone(R.id.layout_strategy, false);
        }
    }

}
