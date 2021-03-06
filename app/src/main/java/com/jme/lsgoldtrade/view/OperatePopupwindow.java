package com.jme.lsgoldtrade.view;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowOperateBinding;

public class OperatePopupwindow extends JMEBasePopupWindow {

    private PopupwindowOperateBinding mBinding;

    public OperatePopupwindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_operate, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String title, String content, String leftStr, String rightStr,
                        View.OnClickListener leftListener, View.OnClickListener rightListener) {
        mBinding.tvTitle.setText(title);
        mBinding.tvContent.setText(content);
        mBinding.btnLeft.setText(leftStr);
        mBinding.btnRight.setText(rightStr);
        mBinding.btnLeft.setOnClickListener(leftListener);
        mBinding.btnRight.setOnClickListener(rightListener);
    }
}
