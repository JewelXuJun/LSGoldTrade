package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCreateConditionSheetBinding;
import com.jme.lsgoldtrade.domain.ConditionOrderRunVo;
import com.jme.lsgoldtrade.service.ConditionService;

import java.util.HashMap;

public class CreateConditionSheetFragment extends JMEBaseFragment {

    private FragmentCreateConditionSheetBinding mBinding;

    private boolean bVisibleToUser = false;
    private int mConditionOrderRunNum;
    private int mStopOrderRunNum;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (bVisibleToUser && null != mBinding)
            queryConditionOrderRun();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            queryConditionOrderRun();
    }

    private void queryConditionOrderRun() {
        sendRequest(ConditionService.getInstance().queryConditionOrderRun, new HashMap<>(), false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QueryConditionOrderRun":
                if (head.isSuccess()) {
                    ConditionOrderRunVo conditionOrderRunVo;

                    try {
                        conditionOrderRunVo = (ConditionOrderRunVo) response;
                    } catch (Exception e) {
                        conditionOrderRunVo = null;

                        e.printStackTrace();
                    }

                    if (null == conditionOrderRunVo)
                        return;

                    mConditionOrderRunNum = conditionOrderRunVo.getConditionOrderRunNum();
                    mStopOrderRunNum = conditionOrderRunVo.getStopOrderRunNum();

                    if (0 == mConditionOrderRunNum && 0 == mStopOrderRunNum) {
                        mBinding.tvRunningMessage.setVisibility(View.GONE);
                    } else {
                        String message;

                        if (0 != mConditionOrderRunNum && 0 == mStopOrderRunNum)
                            message = String.format(mContext.getResources().getString(R.string.transaction_condition_sheet_running_message), mConditionOrderRunNum);
                        else if (0 == mConditionOrderRunNum && 0 != mStopOrderRunNum)
                            message = String.format(mContext.getResources().getString(R.string.transaction_stop_running_message), mStopOrderRunNum);
                        else
                            message = String.format(mContext.getResources().getString(R.string.transaction_all_running_message), mConditionOrderRunNum, mStopOrderRunNum);

                        mBinding.tvRunningMessage.setText(message);
                        mBinding.tvRunningMessage.setVisibility(View.VISIBLE);
                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickConditionOrderRun() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_ORDER_RUN, 0 != mConditionOrderRunNum ? 1 : 2);
        }

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
