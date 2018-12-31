package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentQueryBinding;

public class QueryFragment extends JMEBaseFragment {

    private FragmentQueryBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_query;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentQueryBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvTime.setText(DateUtil.dateToString(System.currentTimeMillis()));
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickQueryDailyStatement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.DAILYSTATEMENT)
                    .navigation();
        }

        public void onClickQueryCurrentHoldPosition() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CURRENTHOLDPOSITION)
                    .navigation();
        }

        public void onClickQueryHistoryHoldPosition() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.HISTORYHOLDPOSITION)
                    .navigation();
        }

        public void onClickQueryCurrentEntrust() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CURRENTENTRUST)
                    .navigation();
        }

        public void onClickQueryHistoryEntrust() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.HISTORYENTRUST)
                    .navigation();
        }

        public void onClickQueryCurrentDeal() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CURRENTDEAL)
                    .navigation();
        }

        public void onClickQueryHistoryDeal() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.HISTORYDEAL)
                    .navigation();
        }
    }
}
