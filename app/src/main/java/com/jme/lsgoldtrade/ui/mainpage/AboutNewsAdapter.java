package com.jme.lsgoldtrade.ui.mainpage;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InfoListVo;
import com.jme.lsgoldtrade.view.MyTextview;

import java.util.List;

public class AboutNewsAdapter extends BaseQuickAdapter<InfoListVo, BaseViewHolder> {

    public AboutNewsAdapter(int resId, List<InfoListVo> infoListVo) {
        super(resId, infoListVo);
    }

    @Override
    protected void convert(BaseViewHolder helper, InfoListVo item) {
        String content = item.getContent();
        MyTextview mytextview = helper.getView(R.id.mytextview);
        mytextview.setText(content);
    }
}
