package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowSignedBinding;

public class SignedPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowSignedBinding mBinding;

    public SignedPopUpWindow(Context context) {
        super(context);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_signed, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.tvSignedMessage.setText(R.string.increment_account_singed_message);

        mBinding.layoutCancel.setOnClickListener((view) -> dismiss());
        mBinding.btnSigned.setOnClickListener((view) -> {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                    .withString("Resource", "Trade")
                    .navigation();

            dismiss();
        });
    }

}
