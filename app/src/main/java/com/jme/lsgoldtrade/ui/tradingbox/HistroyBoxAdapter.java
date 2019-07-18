package com.jme.lsgoldtrade.ui.tradingbox;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.HistoryItemVo;

import java.util.List;

public class HistroyBoxAdapter extends BaseQuickAdapter<HistoryItemVo, BaseViewHolder> {

    private Context context;

    public HistroyBoxAdapter(int layoutResId, List<HistoryItemVo> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

//    @Override
//    protected void convertHead(BaseViewHolder helper, MySection item) {
//        helper.setText(R.id.time, item.header + "期");
//    }

//    @Override
//    protected void convert(BaseViewHolder helper, MySection item) {
//        final SectionBean sectionBean = item.t;
//        helper.setText(R.id.title, sectionBean.getChance());
//        helper.setText(R.id.k_line, sectionBean.getVariety());
//        helper.setText(R.id.time, sectionBean.getPushTime());
//        String pushTime = sectionBean.getPushTime().substring(0, sectionBean.getPushTime().lastIndexOf(" ")).replaceAll("-", "/");
//        helper.setText(R.id.time, "发布时间" + pushTime);
//        helper.setText(R.id.time_1, item.header + "期");
//
//        String direction = sectionBean.getDirection();
//        if ("0".equals(direction)) {
//            helper.setText(R.id.more_kong, "多");
//        } else {
//            helper.setText(R.id.more_kong, "空");
//        }
//    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryItemVo item) {
        helper.setText(R.id.title, "交易机会  " + item.getChance());
        helper.setText(R.id.k_line, item.getVariety());
        helper.setText(R.id.time, item.getPushTime());
        String pushTime = item.getPushTime().substring(0, item.getPushTime().lastIndexOf(" ")).replaceAll("-", "/");
        helper.setText(R.id.time, "发布时间" + pushTime);
        helper.setText(R.id.time_1, item.getPeriodName() + "期");

        String direction = item.getDirection();
        if ("0".equals(direction)) {
            helper.setText(R.id.more_kong, "多");
        } else {
            helper.setText(R.id.more_kong, "空");
        }
    }
}
