package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMoneyOutBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.HashMap;

import rx.Subscription;

public class MoneyOutFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMoneyOutBinding mBinding;

    private boolean bVisibleToUser = false;
    private String mMaxBalance;

    private Subscription mRxbus;
    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_money_out;
    }

    @Override
    protected void initView() {
        super.initView();

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mBinding.etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".") && !s.toString().equals(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));

                        mBinding.etTransferAmount.setText(s);
                        mBinding.etTransferAmount.setSelection(s.length());
                    } else {
                        mBinding.etTransferAmount.setSelection(s.length());
                    }
                } else if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etTransferAmount.setText(s);
                    mBinding.etTransferAmount.setSelection(2);
                } else if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etTransferAmount.setText(s.subSequence(0, 1));
                        mBinding.etTransferAmount.setSelection(1);

                        return;
                    }
                } else {
                    mBinding.etTransferAmount.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.btnSubmit.setEnabled(mBinding.etTransferAmount.length() > 0);
            }
        });

    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentMoneyOutBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (bVisibleToUser && null != mBinding)
            getAccount(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            getAccount(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_ELECTRONICCARD_UPDATE:
                    getAccount(false);

                    break;
            }
        });
    }

    private void doSubmit() {
        String amount = mBinding.etTransferAmount.getText().toString();

        if (amount.endsWith("."))
            amount = amount.substring(0, amount.length() - 1);

        if (TextUtils.isEmpty(mMaxBalance))
            showShortToast(R.string.transaction_amount_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
            showShortToast(R.string.transaction_money_min_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMaxBalance)) == 1)
            showShortToast(R.string.transaction_money_out_max_error);
        else
            getStatus();
    }

    private void getAccount(boolean enable) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().account, params, enable);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    private void inoutMoney(String amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("amount", String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(100)).longValue()));
        params.put("direction", "1");

        sendRequest(TradeService.getInstance().inOutMoney, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Account":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    AccountVo accountVo;

                    try {
                        accountVo = (AccountVo) response;
                    } catch (Exception e) {
                        accountVo = null;

                        e.printStackTrace();
                    }

                    if (null == accountVo)
                        return;

                    BigDecimal total = new BigDecimal(accountVo.getTransactionBalanceStr())
                            .add(new BigDecimal(accountVo.getFreezeBalanceStr()))
                            .add(new BigDecimal(accountVo.getPositionMarginStr()))
                            .add(new BigDecimal(accountVo.getFloatProfitStr()))
                            .subtract(new BigDecimal(accountVo.getRuntimeFeeStr()));
                    BigDecimal totalProfit = total.subtract(new BigDecimal(accountVo.getMinReserveFundStr()));
                    BigDecimal extractableBalance = new BigDecimal(accountVo.getExtractableBalanceStr()).subtract(new BigDecimal(accountVo.getRuntimeFeeStr()));

                    mMaxBalance = String.valueOf(Math.min(totalProfit.doubleValue(), extractableBalance.doubleValue()));

                    if (new BigDecimal(mMaxBalance).compareTo(BigDecimal.ZERO) == -1)
                        mMaxBalance = "0.00";

                    mBinding.tvMoneyOutMax.setText(MarketUtil.decimalFormatMoney(mMaxBalance));
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
            case "GetStatus":
                if (head.isSuccess()) {
                    String status;

                    if (null == response)
                        status = "";
                    else
                        status = response.toString();

                    if (status.equals("1")) {
                        if (null != mTransactionMessagePopUpWindow && !mTransactionMessagePopUpWindow.isShowing()) {
                            mTransactionMessagePopUpWindow.setData(mContext.getResources().getString(R.string.transaction_account_error),
                                    mContext.getResources().getString(R.string.transaction_account_goto_recharge),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.RECHARGE).navigation();

                                        mTransactionMessagePopUpWindow.dismiss();
                                    });
                            mTransactionMessagePopUpWindow.showAtLocation(mBinding.etTransferAmount, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        inoutMoney(mBinding.etTransferAmount.getText().toString());
                    }
                }

                break;
            case "InOutMoney":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_money_out_success);

                    mBinding.etTransferAmount.setText("");

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CAPITALTRANSFER_SUCCESS, null);
                }

                getAccount(true);

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getAccount(false);
    }

    public class ClickHandlers {

        public void onClickOutAll() {
            mBinding.etTransferAmount.setText(mMaxBalance);
            mBinding.etTransferAmount.setSelection(TextUtils.isEmpty(mMaxBalance) ? 0 : mMaxBalance.length());
        }

        public void onClickSubmit() {
            doSubmit();
        }

    }
}
