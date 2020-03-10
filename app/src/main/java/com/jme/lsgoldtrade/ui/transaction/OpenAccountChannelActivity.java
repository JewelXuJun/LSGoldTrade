package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOpenAccountChannelBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.OPENACCOUNTCHANNEL)
public class OpenAccountChannelActivity extends JMEBaseActivity {

    private ActivityOpenAccountChannelBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_open_account_channel;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_select_open_account_channel, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        String bankId = mUser.getCurrentUser().getBankId();

        mBinding.layoutBankHf.setVisibility(!TextUtils.isEmpty(bankId) && bankId.equals("icbc") ? View.GONE : View.VISIBLE);
        mBinding.layoutBankIcbc.setVisibility(!TextUtils.isEmpty(bankId) && bankId.equals("hfb") ? View.GONE : View.VISIBLE);
        mBinding.tvRecommend.setVisibility(TextUtils.isEmpty(bankId) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityOpenAccountChannelBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void getWhetherIdCard() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "WhetherIdCard":
                if (head.isSuccess()) {
                    IdentityInfoVo identityInfoVo;

                    try {
                        identityInfoVo = (IdentityInfoVo) response;
                    } catch (Exception e) {
                        identityInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == identityInfoVo)
                        return;

                    String flag = identityInfoVo.getFlag();

                    if (TextUtils.isEmpty(flag))
                        return;

                    if (flag.equals("Y"))
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.BINDACCOUNT)
                                .withString("Name", identityInfoVo.getName())
                                .withString("IDCard", identityInfoVo.getIdCard())
                                .navigation();
                    else
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.AUTHENTICATION)
                                .withString("Type", "2")
                                .withString("BankId", "icbc")
                                .navigation();
                }

                break;
        }
    }

    public class ClickHandlers {

        private long mLastClickTime;
        private long timeInterval = 1000L;

        public void onClickOpenAccountHF() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.AUTHENTICATION)
                    .withString("Type", "1")
                    .withString("BankId", "hfb")
                    .navigation();
        }

        public void onClickOpenAccountICBC() {
            long nowTime = System.currentTimeMillis();

            if (nowTime - mLastClickTime > timeInterval) {
                mLastClickTime = nowTime;

                if (null == mUser || !mUser.isLogin())
                    gotoLogin();
                else
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.AUTHENTICATION)
                            .withString("Type", "1")
                            .withString("BankId", "icbc")
                            .navigation();
            }
        }

        public void onClickBindAccountICBC() {
            long nowTime = System.currentTimeMillis();

            if (nowTime - mLastClickTime > timeInterval) {
                mLastClickTime = nowTime;

                if (null == mUser || !mUser.isLogin())
                    gotoLogin();
                else
                    getWhetherIdCard();
            }
        }

        public void onClickCourseHF() {

        }

        public void onClickCourseICBC() {

        }

    }
}
