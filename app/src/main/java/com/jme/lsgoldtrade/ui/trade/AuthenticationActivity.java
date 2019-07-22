package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.IDUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAuthenticationBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.IntentUtils;

import java.util.HashMap;

import rx.Subscription;

/**
 * 身份验证
 */
@Route(path = Constants.ARouterUriConst.AUTHENTICATION)
public class AuthenticationActivity extends JMEBaseActivity {

    private ActivityAuthenticationBinding mBinding;

    private String mType;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_authentication, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getStringExtra("Type");

        mBinding.btnBind.setText(mType.equals("1") ? R.string.register_open_account : R.string.text_next);

        getWhetherIdCard();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityAuthenticationBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_BIND_SUCCESS:
                    finish();

                    break;
            }
        });
    }

    private void getWhetherIdCard() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
    }

    private void verifyIdCard(String name, String idCard) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("idCard", idCard);

        sendRequest(TradeService.getInstance().verifyIdCard, params, true);
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

                    mBinding.etName.setText(identityInfoVo.getName());
                    mBinding.etName.setEnabled(false);
                    mBinding.etName.setFocusable(false);

                    mBinding.etIdCard.setText(identityInfoVo.getIdCard());
                    mBinding.etIdCard.setEnabled(false);
                    mBinding.etIdCard.setFocusable(false);
                }

                break;
            case "VerifyIdCard":
                if (head.isSuccess()) {
                    if (mType.equals("1"))
                        IntentUtils.jumpBankSmall(this);
                    else
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.BINDACCOUNT)
                                .withString("Name", mBinding.etName.getText().toString().trim())
                                .withString("IDCard", mBinding.etIdCard.getText().toString().trim())
                                .navigation();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickBind() {
            String name = mBinding.etName.getText().toString().trim();
            String idCard = mBinding.etIdCard.getText().toString().trim();
            String errorMsg = IDUtils.IDCardValidate(idCard);

            if (TextUtils.isEmpty(name))
                showShortToast(R.string.trade_name_hint);
            else if (TextUtils.isEmpty(idCard))
                showShortToast(R.string.trade_id_card_hint);
            else if (!TextUtils.isEmpty(errorMsg))
                showShortToast(errorMsg);
            else
                verifyIdCard(name, idCard);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
