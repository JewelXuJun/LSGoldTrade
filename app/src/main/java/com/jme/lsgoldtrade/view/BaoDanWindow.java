package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowBaodanBinding;
import com.jme.lsgoldtrade.util.MarketUtil;

public class BaoDanWindow extends JMEBasePopupWindow {

    private PopupwindowBaodanBinding mBinding;

    public BaoDanWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_baodan
                , null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(String account, String code, String price, String amount, String type, View.OnClickListener confirmListener) {
        mBinding.title.setText(type);
        mBinding.backUsername.setText(account);
        mBinding.weituojiage.setText(MarketUtil.decimalFormatMoney(price));
        mBinding.weituoshuliang.setText(amount + "手");
        mBinding.businessVarieties.setText(code);

        mBinding.btnConfirm.setOnClickListener(confirmListener);

        if ("1".equals(type)) {
            mBinding.title.setText("买多报单确认");
            mBinding.businessType.setText("买入开仓");
        } else if ("2".equals(type)) {
            mBinding.title.setText("卖空报单确认");
            mBinding.businessType.setText("卖出开仓");
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }
}
