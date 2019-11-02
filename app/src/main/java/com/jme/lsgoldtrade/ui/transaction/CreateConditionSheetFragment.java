package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentCreateConditionSheetBinding;

public class CreateConditionSheetFragment extends JMEBaseFragment {

    private FragmentCreateConditionSheetBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_create_condition_sheet;
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

        mBinding = (FragmentCreateConditionSheetBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickSelectContract() {

        }

        public void onClickBuyMore() {

        }

        public void onClickSellEmpty() {

        }

        public void onClickPriceMinus() {

        }

        public void onClickPriceAdd() {

        }

        public void onClickEntrustTypeTips() {

        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onClickEffectiveTimeTips() {

        }

        public void onCliclConditionSheetRiskTips() {

        }

        public void onClickSubmit() {

        }

    }
}
