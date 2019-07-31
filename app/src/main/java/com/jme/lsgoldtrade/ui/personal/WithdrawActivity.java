package com.jme.lsgoldtrade.ui.personal;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithdrawBinding;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/31 18:20
 * Desc   : 提现页面
 */
@Route(path = Constants.ARouterUriConst.WITHDRAW)
public class WithdrawActivity extends JMEBaseActivity {

    private ActivityWithdrawBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("提现", true);
        mBinding = (ActivityWithdrawBinding) mBindingUtil;
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickWithdraw() {

        }

    }
}
