package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowMarketOrderBinding;
import com.jme.lsgoldtrade.domain.TenSpeedVo;

/**
 * Created by XuJun on 2018/12/2.
 */
public class MarketOrderPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowMarketOrderBinding mBinding;

    public MarketOrderPopUpWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(DensityUtil.dpTopx(getContext(), 230));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_market_order, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(TenSpeedVo tenSpeedVo) {
        if (null == tenSpeedVo)
            return;

        mBinding.tvLimitDownPrice.setText(tenSpeedVo.getLowerLimitPrice());
        mBinding.tvLimitUpPrice.setText(tenSpeedVo.getHighLimitPrice());
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

        public void onClickLimitDownPrice() {

        }

        public void onClickLimitUpPrice() {

        }

        public void onClickPriceMinus() {

        }

        public void onClickPriceAdd() {

        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onClickRivalPrice() {

        }

        public void onClickQueuingPrice() {

        }

        public void onClickLastPrice() {

        }


        public void onClickBuyMore() {

        }

        public void onClickSaleEmpty() {

        }

        public void onClickEqualMore() {

        }

        public void onClickEqualEmpty() {

        }

    }

}
