package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.Gravity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityValueAddedServiceBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.OpenServicePopupWindow;
import java.util.HashMap;

/**
 * 开通增值服务
 */
@Route(path = Constants.ARouterUriConst.VALUEADDEDSERVICE)
public class ValueAddedServiceActivity extends JMEBaseActivity {

    private ActivityValueAddedServiceBinding mBinding;

    private int isAgree = 1;

    private OpenServicePopupWindow mWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_value_added_service;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("开通增值服务", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mWindow = new OpenServicePopupWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);

        mBinding.cbAgree.setChecked(true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isAgree = 1;
            } else {
                isAgree = 0;
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityValueAddedServiceBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickTradingBoxAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "交易匣子服务协议")
                    .withString("url", Constants.HttpConst.URL_TRADING_BOX_AGREEMENT)
                    .navigation();
        }

        public void onClickEntrustRiskAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "委托风控服务协议")
                    .withString("url", Constants.HttpConst.URL_ENTRUST_RISK_AGREEMENT)
                    .navigation();
        }

        public void onClickOpenService() {
            if (isAgree == 0)
                showShortToast("请先阅读并同意《交易匣子服务协议》《委托风控服务协议》");
            else
                sendRequest(ManagementService.getInstance().openValueAddedServices, new HashMap<>(), true);
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "OpenValueAddedServices":
                if (head.isSuccess()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.VALUESERVICESUCCESS)
                            .navigation();
                    this.finish();
                } else {
                    if (head.getMsg().contains("需要在泰金所完成2笔以上的交易才能开通增值服务")) {
                        mToast.cancel();
                        if (null != mWindow && !mWindow.isShowing()) {
                            mWindow.setData("开通增值服务需要至少完成两笔交易，您尚不满足开通增值服务条件，请满足条件后申请。", (view) -> {
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                            });
                            mWindow.showAtLocation(mBinding.lltitle, Gravity.CENTER, 0, 0);
                        }
                    } else if (head.getMsg().contains("银行资金账户不存在")) {
                        mToast.cancel();
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData("您尚未绑定黄金账户，请先开户绑定后再使用该服务。", "确定",(view) -> {
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                            });
                            mConfirmPopupwindow.showAtLocation(mBinding.lltitle, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        showShortToast(head.getMsg());
                    }
                }
                break;
        }
    }
}
