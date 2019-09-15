package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCashBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.domain.WithDraw;
import com.jme.lsgoldtrade.service.AccountService;

import java.util.HashMap;

/**
 * 提现
 */
@Route(path = Constants.ARouterUriConst.CASH)
public class CashActivity extends JMEBaseActivity {

    private ActivityCashBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cash;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("提现申请", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getDateFromNet();
    }

    private void getDateFromNet() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), false);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityCashBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickAll() {
            String banlace = mBinding.banlace.getText().toString().trim().replace("元", "");
            mBinding.etGoldAccount.setText(banlace);
        }

        public void onClickCash() {
            String price = mBinding.etGoldAccount.getText().toString().trim();
            if (TextUtils.isEmpty(price) || price.contains("-") || "0".equals(price)) {
                showShortToast("请输入正确的价格");
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("amount", price);
            sendRequest(AccountService.getInstance().withdraw, params, false);
        }
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

                    mBinding.banlace.setText(value.getBalance());
                }
                break;
            case "Withdraw":

                if (head.isSuccess()) {
                    WithDraw value;
                    try {
                        value = (WithDraw) response;
                    } catch (Exception e) {
                        value = null;

                        e.printStackTrace();
                    }
                    if (value == null)
                        return;


                }

                //Application for cash
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.APPLICATIONFORCASH)
                        .navigation();
                break;
        }
    }
}
