package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckServiceBinding;

/**
 * 查看增值服务
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
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickRecharge() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RECHARGE)
                    .navigation();
        }

        public void onClickCash() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CASH)
                    .navigation();
        }

        public void onClickThaw() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.THAW)
                    .navigation();
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
