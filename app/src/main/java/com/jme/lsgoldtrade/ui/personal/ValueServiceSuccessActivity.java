package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityValueServiceSuccessBinding;

/**
 * 增值服务开通成功
 */
@Route(path = Constants.ARouterUriConst.VALUESERVICESUCCESS)
public class ValueServiceSuccessActivity extends JMEBaseActivity {

    private ActivityValueServiceSuccessBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_value_service_success;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityValueServiceSuccessBinding) mBindingUtil;
        initToolbar("开通增值服务", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {
        public void onClickChongZhi() {//充值
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RECHARGE)
                    .navigation();
            finish();
        }

        public void onClickCheckService() {//查看服务
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CHECKSERVICE)
                    .navigation();
            finish();
        }
    }
}
