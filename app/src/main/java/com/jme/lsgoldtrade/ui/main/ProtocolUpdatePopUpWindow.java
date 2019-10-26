package com.jme.lsgoldtrade.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowProtocolUpdateBinding;
import com.jme.lsgoldtrade.domain.ProtocolVo;

import java.util.List;

public class ProtocolUpdatePopUpWindow extends JMEBasePopupWindow {

    private PopupwindowProtocolUpdateBinding mBinding;

    private Context mContext;

    private ProtocolAdapter mAdapter;

    private String mName;
    private String mCardNo;

    public ProtocolUpdatePopUpWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 320));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_protocol_update, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mAdapter = new ProtocolAdapter(getContext(), R.layout.item_protocol, null);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProtocolVo protocolVo = (ProtocolVo) adapter.getItem(position);

            if (null == protocolVo)
                return;

            ProtocolVo.ZprotocolTypeBean zprotocolTypeBean = protocolVo.getZprotocolTypeList();

            if (null == zprotocolTypeBean)
                return;

            String code = zprotocolTypeBean.getCode();
            String protocolType = zprotocolTypeBean.getProtocolType();
            String protocolUrl = zprotocolTypeBean.getProtocolUrl();

            if (TextUtils.isEmpty(protocolUrl))
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", protocolType)
                    .withString("url", !TextUtils.isEmpty(code) && (code.equals("RJ") || code.equals("DL")) ?
                            protocolUrl + "?name=" + mName + "&cardNo=" + mCardNo : protocolUrl)
                    .navigation();
        });
    }

    public void setData(List<ProtocolVo> protocolVoList, String name, String cardNo, View.OnClickListener agreeListener) {
        mName = name;
        mCardNo = cardNo;

        mAdapter.setNewData(protocolVoList);
        mBinding.btnAgree.setOnClickListener(agreeListener);
    }

}
