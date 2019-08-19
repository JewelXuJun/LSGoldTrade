package com.jme.lsgoldtrade.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.ProtocolVo;

import java.util.List;

public class ProtocolAdapter extends BaseQuickAdapter<ProtocolVo, BaseViewHolder> {

    private Context mContext;

    public ProtocolAdapter(Context context, int layoutResId, @Nullable List<ProtocolVo> data) {
        super(layoutResId, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProtocolVo item) {
        if (null == item)
            return;

        ProtocolVo.ZprotocolTypeBean zprotocolTypeBean = item.getZprotocolTypeList();

        if (null == zprotocolTypeBean)
            return;

        helper.setText(R.id.tv_protocol, String.format(mContext.getResources().getString(R.string.main_protocol), zprotocolTypeBean.getProtocolType()));
    }
}
