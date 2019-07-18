package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRechargeBinding;
import com.jme.lsgoldtrade.view.PayPopupwindow;

/**
 * 去充值
 */
@Route(path = Constants.ARouterUriConst.RECHARGE)
public class RechargeActivity extends JMEBaseActivity {

    private ActivityRechargeBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("在线充值", true);
        mBinding = (ActivityRechargeBinding) mBindingUtil;
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

        public void onClickPay() {
            String chongzhi = mBinding.etGoldAccount.getText().toString().trim();
            if (TextUtils.isEmpty(chongzhi)) {
                showShortToast("请输入充值金额");
                return;
            }
            PayPopupwindow.popupwindow(RechargeActivity.this, R.id.et_gold_account);
        }
    }
}
