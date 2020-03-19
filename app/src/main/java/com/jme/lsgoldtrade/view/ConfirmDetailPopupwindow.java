package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowConfirmDetailBinding;

public class ConfirmDetailPopupwindow extends JMEBasePopupWindow {

    private PopupwindowConfirmDetailBinding mBinding;

    public ConfirmDetailPopupwindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_confirm_detail, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.btnCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String title, String content, View.OnClickListener confirmListener) {
        mBinding.tvTitle.setText(title);
        mBinding.tvContent.setText(content);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }
}
