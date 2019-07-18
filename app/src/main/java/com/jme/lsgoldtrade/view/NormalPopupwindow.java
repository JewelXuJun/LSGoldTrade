package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowNormalBinding;

public class NormalPopupwindow extends JMEBasePopupWindow {
    private PopupwindowNormalBinding mBinding;

    public NormalPopupwindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 260));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_normal, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String code, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        mBinding.tvContent.setText(code);
        mBinding.btnCancel.setOnClickListener(cancelListener);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }
}
