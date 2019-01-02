package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
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

import java.util.HashMap;

public class QueryFragment extends JMEBaseFragment {

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

       /* if (null != mBinding && bVisibleToUser)
            dailystatement();*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (bVisibleToUser)
            dailystatement();*/
    }

    private void setDailyStatementData(DailyStatementVo dailyStatementVo) {

    }

    private String getTradeDate() {
        String date = mBinding.tvTime.getText().toString();
        String value;

        if (TextUtils.isEmpty(date))
            value = "";
        else if (date.contains("-"))
            value = date.replace("-", "");
        else
            value = date;

        return value;
    }

    private void dailystatement() {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeDate", getTradeDate());

        sendRequest(TradeService.getInstance().dailystatement, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DailyStatement":
                if (head.isSuccess()) {
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
                }

                break;
        }
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
