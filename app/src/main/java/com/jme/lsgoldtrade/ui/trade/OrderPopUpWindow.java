package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowMarketOrderBinding;
import com.jme.lsgoldtrade.databinding.PopupwindowOrderBinding;
import com.jme.lsgoldtrade.util.MarketUtil;


public class OrderPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowOrderBinding mBinding;

    public OrderPopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_order, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(String account, String code, String price, String amount, String type, View.OnClickListener confirmListener) {
        mBinding.tvAccount.setText(account);
        mBinding.tvCode.setText(code);
        mBinding.tvPrice.setText(MarketUtil.decimalFormatMoney(price) + getContext().getResources().getString(R.string.text_money_unit));
        mBinding.tvAmount.setText(amount + getContext().getResources().getString(R.string.text_amount_unit));
        mBinding.tvType.setText(type);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }

}
