package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowTransactionMessageBinding;

public class TransactionMessagePopUpWindow extends JMEBasePopupWindow {

    private PopupwindowTransactionMessageBinding mBinding;

    public TransactionMessagePopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_transaction_message, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.tvCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String message, String confirm, View.OnClickListener confirmListener) {
        mBinding.tvMessage.setText(message);
        mBinding.tvConfirm.setText(confirm);
        mBinding.tvConfirm.setOnClickListener(confirmListener);
    }

}
