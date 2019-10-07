package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentPlaceOrderBinding;

public class PlaceOrderFragment extends JMEBaseFragment {

    private FragmentPlaceOrderBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_place_order;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentPlaceOrderBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickSelectContract() {

        }

        public void onClickLimitDownPrice() {

        }

        public void onClickLimitUpPrice() {

        }

        public void onClickLatestPrice() {

        }

        public void onClickPriceMinus() {

        }

        public void onClickPriceAdd() {

        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onClickBuyMore() {

        }

        public void onClickSaleEmpty() {

        }
    }
}
