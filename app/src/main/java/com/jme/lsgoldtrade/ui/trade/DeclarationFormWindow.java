package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowDeclarationFormBinding;

public class DeclarationFormWindow extends JMEBasePopupWindow {

    private PopupwindowDeclarationFormBinding mBinding;

    public DeclarationFormWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_declaration_form, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());

        mBinding.btnCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String account, String contractID, String price, String amount, String type, View.OnClickListener confirmListener) {
        mBinding.tvTitle.setText(type.equals("1") ? mContext.getString(R.string.trade_buy_more_confirm)
                : mContext.getString(R.string.trade_sale_empty_confirm));
        mBinding.tvGoldAccount.setText(account);
        mBinding.tvBusinessType.setText(type.equals("1") ? mContext.getString(R.string.trade_buy_open)
                : mContext.getString(R.string.trade_sale_open));
        mBinding.tvBusinessVarieties.setText(contractID);
        mBinding.tvPrice.setText(price);
        mBinding.tvAmount.setText(String.format(mContext.getResources().getString(R.string.trade_amount_unit), amount));

        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }

}
