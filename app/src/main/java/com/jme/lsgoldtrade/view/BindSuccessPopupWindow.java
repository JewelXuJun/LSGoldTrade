package com.jme.lsgoldtrade.view;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowBindSuccessBinding;

public class BindSuccessPopupWindow extends JMEBasePopupWindow {

    private PopupwindowBindSuccessBinding mBinding;

    private Context mContext;

    public BindSuccessPopupWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(DensityUtil.dpTopx(getContext(), 285));
        setWidth(DensityUtil.dpTopx(getContext(), 335));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_bind_success, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String time) {
        mBinding.tvTime.setText(String.format(mContext.getResources().getString(R.string.transaction_bind_success_time), time));
    }
}
