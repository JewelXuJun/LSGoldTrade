package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.IDUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAuthenticationBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.IntentUtils;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

import rx.Subscription;

/**
 * 身份验证
 */
@Route(path = Constants.ARouterUriConst.AUTHENTICATION)
public class AuthenticationActivity extends JMEBaseActivity {

    private ActivityAuthenticationBinding mBinding;

    private String mType;
    private String mBankId;
    private String mIDCard;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_authentication, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getStringExtra("Type");
        mBankId = getIntent().getStringExtra("BankId");

        mBinding.btnBind.setText(mType.equals("1") ? R.string.register_open_account : R.string.text_next);
        mBinding.layoutMobile.setVisibility(!TextUtils.isEmpty(mBankId) && mBankId.equals("hfb") ? View.VISIBLE : View.GONE);

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

    private void getHFBankOpenAccountUrl() {
        HashMap<String, String> params = new HashMap<>();
        params.put("customerName", mBinding.etName.getText().toString());
        params.put("idCode", mIDCard);
        params.put("mobile", mBinding.etMobile.getText().toString());

        sendRequest(TradeService.getInstance().getHFBankOpenAccountUrl, params, true);
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

                    if (flag.equals("Y")) {
                        mBinding.etName.setText(identityInfoVo.getName());
                        mBinding.etName.setEnabled(false);
                        mBinding.etName.setFocusable(false);

                        mIDCard = identityInfoVo.getIdCard();

                        mBinding.etIdCard.setText(StringUtils.formatIDCardNumber(mIDCard));
                        mBinding.etIdCard.setEnabled(false);
                        mBinding.etIdCard.setFocusable(false);
                    }
                }

                if (!TextUtils.isEmpty(mBankId) && mBankId.equals("hfb"))
                    mBinding.etMobile.setText(mUser.getCurrentUser().getNoMaskMobile());

                break;
            case "VerifyIdCard":
                if (head.isSuccess()) {
                    if (mType.equals("1")) {
                        if (!TextUtils.isEmpty(mBankId) && mBankId.equals("hfb"))
                            getHFBankOpenAccountUrl();
                        else
                            IntentUtils.intentICBCSmall(this);
                    } else {
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.BINDACCOUNT)
                                .withString("Name", mBinding.etName.getText().toString().trim())
                                .withString("IDCard", mIDCard)
                                .navigation();
                    }
                }

                break;
            case "GetHFBankOpenAccountUrl":
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.OPENACCOUNTHFWEBVIEW)
                        .withString("url", (String) response)
                        .navigation();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickBind() {
            String name = mBinding.etName.getText().toString().trim();
            String mobile = mBinding.etMobile.getText().toString();

            if (TextUtils.isEmpty(mIDCard))
                mIDCard = mBinding.etIdCard.getText().toString().trim();

            String errorMsg = IDUtils.IDCardValidate(mIDCard);

            if (TextUtils.isEmpty(name))
                showShortToast(R.string.transaction_name_hint);
            else if (TextUtils.isEmpty(mIDCard))
                showShortToast(R.string.transaction_id_card_hint);
            else if (!TextUtils.isEmpty(errorMsg))
                showShortToast(errorMsg);
            else if (!TextUtils.isEmpty(mBankId) && mBankId.equals("hfb") && !ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else
                verifyIdCard(name, mIDCard);
        }

        public void onClickModify() {
            mBinding.etMobile.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
