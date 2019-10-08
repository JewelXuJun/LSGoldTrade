package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckServiceBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.service.AccountService;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 我的增值服务
 */
@Route(path = Constants.ARouterUriConst.CHECKSERVICE)
public class CheckServiceActivity extends JMEBaseActivity {

    private ActivityCheckServiceBinding mBinding;

    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_service;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("我的增值服务", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(this);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityCheckServiceBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserInfo();
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    private void getUserInfo() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), true);
    }

    private void hasWeChatWithdrawAuth() {
        sendRequest(AccountService.getInstance().hasWeChatWithdrawAuth, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetUserInfo":
                if (head.isSuccess()) {
                    UsernameVo value;
                    try {
                        value = (UsernameVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }
                    if (value == null)
                        return;

                    mBinding.tvAvailableFunds.setText(TextUtils.isEmpty(value.getBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(value.getBalance()).divide(new BigDecimal(100)).toPlainString()));
                    mBinding.tvFrozenFunds.setText(TextUtils.isEmpty(value.getFrozenBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(value.getFrozenBalance()).divide(new BigDecimal(100)).toPlainString()));
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
                            mTransactionMessagePopUpWindow.showAtLocation(mBinding.tvAvailableFunds, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        hasWeChatWithdrawAuth();
                    }
                }

                break;
            case "HasWeChatWithdrawAuth":
                if (head.isSuccess()) {
                    String authFlag;
                    try {
                        authFlag = (String) response;
                    } catch (Exception e) {
                        authFlag = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(authFlag))
                        return;

                    if (authFlag.equals("T"))   // 非首次
                        ARouter.getInstance().build(Constants.ARouterUriConst.WITHDRAW).navigation();
                    else
                        ARouter.getInstance().build(Constants.ARouterUriConst.CHECKUSERINFO).navigation();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickRecharge() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RECHARGE)
                    .navigation();
        }

        public void onClickWithdraw() {
            getStatus();
        }

        public void onClickThaw() {
            showShortToast(R.string.personal_expect);
//            ARouter.getInstance()
//                    .build(Constants.ARouterUriConst.THAW)
//                    .navigation();
        }

        public void onClickDetailed() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.DETAILS)
                    .navigation();
        }

        public void onClickTradingBox() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
        }

        public void onClickEntrust() {
            finish();
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS, null);
        }

        public void onClickService() {

        }
    }
}
