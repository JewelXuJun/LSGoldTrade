package com.jme.lsgoldtrade.ui.tradingbox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

        helper.setText(R.id.tv_content, item.getContent());
    }
}
