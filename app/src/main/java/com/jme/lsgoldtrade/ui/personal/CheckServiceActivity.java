package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckServiceBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.service.AccountService;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 我的增值服务
 */
@Route(path = Constants.ARouterUriConst.CHECKSERVICE)
public class CheckServiceActivity extends JMEBaseActivity {

    private ActivityCheckServiceBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_service;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("我的增值服务", true);
        mBinding = (ActivityCheckServiceBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), true);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
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
            default:
                break;
        }
    }

    public class ClickHandlers {

        public void onClickRecharge() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RECHARGE)
                    .navigation();
        }

        public void onClickCash() {
            showShortToast(R.string.personal_expect);
//            ARouter.getInstance()
//                    .build(Constants.ARouterUriConst.CASH)
//                    .navigation();
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

        }

        public void onClickEntrust() {

        }

        public void onClickService() {

        }
    }
}
