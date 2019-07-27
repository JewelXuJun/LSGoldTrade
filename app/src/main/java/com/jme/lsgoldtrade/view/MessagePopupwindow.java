package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowMessageBinding;

public class MessagePopupwindow extends JMEBasePopupWindow {

    private PopupwindowMessageBinding mBinding;

    public MessagePopupwindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 295));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_message, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String content, String cancel, String conrfirm, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        mBinding.tvContent.setText(content);
        mBinding.btnCancel.setText(cancel);
        mBinding.btnCancel.setOnClickListener(cancelListener);
        mBinding.btnConfirm.setText(conrfirm);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }
}
