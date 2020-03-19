package com.jme.lsgoldtrade.view;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowPlaceOrderBinding;

public class PlaceOrderPopupWindow extends JMEBasePopupWindow {

    private PopupwindowPlaceOrderBinding mBinding;

    public PlaceOrderPopupWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_place_order, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());

        mBinding.btnCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String account, String contractID, String price, String amount, String type, View.OnClickListener confirmListener) {
        mBinding.tvTitle.setText(type.equals("1") ? mContext.getString(R.string.transaction_buy_more_confirm)
                : mContext.getString(R.string.transaction_sale_empty_confirm));
        mBinding.tvGoldAccount.setText(account);
        mBinding.tvBusinessType.setText(type.equals("1") ? mContext.getString(R.string.transaction_buy_open)
                : mContext.getString(R.string.transaction_sale_open));
        mBinding.tvBusinessVarieties.setText(contractID);
        mBinding.tvPrice.setText(price);
        mBinding.tvAmount.setText(String.format(mContext.getResources().getString(R.string.transaction_amount_unit), amount));

        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }

}
