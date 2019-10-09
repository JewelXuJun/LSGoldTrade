package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentHoldPositionsBinding;

public class HoldPositionsFragment extends JMEBaseFragment {

    private FragmentHoldPositionsBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_hold_positions;
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

        mBinding = (FragmentHoldPositionsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickTotalEquity() {

        }

        public void onClickGuaranteeFundSetting() {

        }

        public void onClickRiskRateTips() {

        }

        public void onClickDealQuery() {

        }

        public void onClickInOutMoney() {

        }

        public void onClickEntrustRiskManagement() {

        }

        public void onClickDailyStatementSheet() {

        }

        public void onClickConditionSheet() {

        }

    }
}
