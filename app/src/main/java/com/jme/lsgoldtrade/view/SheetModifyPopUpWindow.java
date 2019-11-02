package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowSheetModifyBinding;
import com.jme.lsgoldtrade.databinding.PopupwindowTransactionMessageBinding;

public class SheetModifyPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowSheetModifyBinding mBinding;

    public SheetModifyPopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_sheet_modify, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public class ClickHandlers {

        public void onClickPriceMinus() {

        }

        public void onClickPriceAdd() {

        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onCliclConditionSheetRiskTips() {

        }

        public void onClickCancel() {

        }

        public void onClickModify() {

        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.tvContractName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
