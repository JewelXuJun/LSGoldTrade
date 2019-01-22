package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentQueryBinding;
import com.jme.lsgoldtrade.domain.DailyStatementVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;

public class QueryFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentQueryBinding mBinding;

    private boolean bVisibleToUser = false;

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

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser)
            dailystatement(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            dailystatement(true);
    }

    private void setDailyStatementData(DailyStatementVo dailyStatementVo) {
        mBinding.tvTodayFloat.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTodayProfitStr()));
        mBinding.tvFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTradingFeeStr()));
        mBinding.tvSoftwareServiceFee.setText(R.string.text_no_data_default);
        mBinding.tvDeferredFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTdDeferFeeStr()));
    }

    private void dailystatement(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("tradeDate", mBinding.tvTime.getText().toString());

        sendRequest(TradeService.getInstance().dailystatement, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DailyStatement":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    DailyStatementVo dailyStatementVo;

                    try {
                        dailyStatementVo = (DailyStatementVo) response;
                    } catch (Exception e) {
                        dailyStatementVo = null;

                        e.printStackTrace();
                    }

                    if (null == dailyStatementVo)
                        return;

                    setDailyStatementData(dailyStatementVo);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        dailystatement(false);
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
