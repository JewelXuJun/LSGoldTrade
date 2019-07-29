package com.jme.lsgoldtrade.ui.tradingbox;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;

import java.util.List;

public class RelevantInfoAdapter extends BaseQuickAdapter<TradingBoxDetailsVo.RelevantInfoListVosBean, BaseViewHolder> {

    public RelevantInfoAdapter(int resId, List<TradingBoxDetailsVo.RelevantInfoListVosBean> infoListVo) {
        super(resId, infoListVo);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingBoxDetailsVo.RelevantInfoListVosBean item) {
        if (null == item)
            return;

        TextView tv_content = helper.getView(R.id.tv_content);
        ImageView img_content_open = helper.getView(R.id.img_content_open);
        ImageView img_content_close = helper.getView(R.id.img_content_close);

        tv_content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tv_content.getViewTreeObserver().removeOnPreDrawListener(this);

                int lineCount = tv_content.getLineCount();

                if (lineCount > 2) {
                    img_content_open.setVisibility(View.VISIBLE);
                    img_content_close.setVisibility(View.GONE);
                    tv_content.setMaxLines(3);
                } else {
                    img_content_open.setVisibility(View.GONE);
                    img_content_close.setVisibility(View.GONE);
                }

                return true;
            }
        });

        img_content_open.setOnClickListener((view) -> {
            tv_content.setSingleLine(false);
            img_content_open.setVisibility(View.GONE);
            img_content_close.setVisibility(View.VISIBLE);
        });

        img_content_close.setOnClickListener((view) -> {
            tv_content.setMaxLines(3);
            img_content_open.setVisibility(View.VISIBLE);
            img_content_close.setVisibility(View.GONE);
        });

        helper.setText(R.id.tv_content, item.getContent());
    }
}
