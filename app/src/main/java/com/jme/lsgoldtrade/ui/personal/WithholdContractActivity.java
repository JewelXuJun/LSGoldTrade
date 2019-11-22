package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.RxBus;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithholdContractBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.WithholdAccountService;
import com.jme.lsgoldtrade.util.ValueUtils;
import com.jme.lsgoldtrade.view.BankPopUpWindow;
import com.jme.lsgoldtrade.view.WithholdMessagePopUpWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.WITHHOLDCONTRACT)
public class WithholdContractActivity extends JMEBaseActivity {

    private ActivityWithholdContractBinding mBinding;

    private boolean bFlag = false;
    private String mResource;
    private String mName;
    private String mIDCard;

    private List<BankVo> mBankVoList = new ArrayList<>();

    private BankPopUpWindow mBankPopUpWindow;
    private WithholdMessagePopUpWindow mWithholdMessagePopUpWindow;
    private JMECountDownTimer mCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withhold_contract;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_account_withhold_contract, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mResource = getIntent().getStringExtra("Resource");

        mBankPopUpWindow = new BankPopUpWindow(this);

        mWithholdMessagePopUpWindow = new WithholdMessagePopUpWindow(this);
        mWithholdMessagePopUpWindow.setOutsideTouchable(false);
        mWithholdMessagePopUpWindow.setFocusable(false);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.transaction_get_verification_code));

        getWhetherIdCard();
        getBanks("");
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.etBankCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence value, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String bankCard = editable.toString();

                if (TextUtils.isEmpty(bankCard)) {
                    mBinding.tvBankName.setText("");
                } else {
                    int length = bankCard.length();

                    if (length == 6)
                        getBanks(bankCard);
                    else if (length < 6)
                        mBinding.tvBankName.setText("");
                }
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityWithholdContractBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.tvName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void getWhetherIdCard() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
    }

    private void getBanks(String cardNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cardNo", cardNo);

        sendRequest(WithholdAccountService.getInstance().getBanks, params, false, false, false);
    }

    private void sendSignMessage(String name, String idCard, String bankCard, String mobile) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("acName", name);
        params.put("idNo", idCard);
        params.put("acNo", bankCard);
        params.put("mobile", mobile);

        sendRequest(WithholdAccountService.getInstance().sendSignMessage, params, true);
    }

    private void sign(String name, String idCard, String bankCard, String mobile, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("acName", name);
        params.put("idNo", idCard);
        params.put("acNo", bankCard);
        params.put("mobile", mobile);
        params.put("validateCode", verifyCode);

        sendRequest(WithholdAccountService.getInstance().sign, params, true);
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

                    mName = identityInfoVo.getName();
                    mIDCard = identityInfoVo.getIdCard();

                    mBinding.tvName.setText(StringUtils.fromatName(mName));
                    mBinding.tvIdCard.setText(StringUtils.formatIDCardNumber(mIDCard));
                }

                break;
            case "GetBanks":
                if (head.isSuccess()) {
                    List<BankVo> bankVoList;

                    try {
                        bankVoList = (List<BankVo>) response;
                    } catch (Exception e) {
                        bankVoList = null;

                        e.printStackTrace();
                    }

                    String cardNo = request.getParams().get("cardNo");

                    if (TextUtils.isEmpty(cardNo)) {
                        mBankVoList.clear();
                        mBankVoList.addAll(bankVoList);
                    } else {
                        if (null != bankVoList && 0 != bankVoList.size()) {
                            BankVo bankVo = bankVoList.get(0);

                            if (null != bankVo)
                                mBinding.tvBankName.setText(bankVo.getBankName());
                        } else {
                            mBinding.tvBankName.setText("");
                        }
                    }
                }

                break;
            case "SendSignMessage":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "Sign":
                if (head.isSuccess()) {
                    if (null != mWithholdMessagePopUpWindow && !mWithholdMessagePopUpWindow.isShowing()) {
                        mWithholdMessagePopUpWindow.setData(getResources().getString(R.string.increment_account_withhold_success),
                                (view) -> {
                                    mWithholdMessagePopUpWindow.dismiss();

                                    if (mResource.equals("Trade")) {
                                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                                    }

                                    finish();
                                });
                        mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvName, Gravity.CENTER, 0, 0);
                    }
                } /*else {
                    if (null != mWithholdMessagePopUpWindow && !mWithholdMessagePopUpWindow.isShowing()) {
                        mWithholdMessagePopUpWindow.setData(getResources().getString(R.string.increment_account_withhold_fail),
                                (view) -> mWithholdMessagePopUpWindow.dismiss());
                        mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvName, Gravity.CENTER, 0, 0);
                    }
                }*/

                break;
        }
    }

    public class ClickHandlers {

        public void onClickBankList() {
            hiddenKeyBoard();

            if (null != mBankPopUpWindow && !mBankPopUpWindow.isShowing()) {
                mBankPopUpWindow.setData(mBankVoList);
                mBankPopUpWindow.showAtLocation(mBinding.tvBankName, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickGetVerificationCode() {
            hiddenKeyBoard();

            String bankCard = mBinding.etBankCard.getText().toString();
            String mobile = mBinding.etMobile.getText().toString();

            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mIDCard))
                showShortToast(R.string.increment_identity_information_error);
            else if (TextUtils.isEmpty(bankCard))
                showShortToast(R.string.increment_bank_card_input);
            else if (bankCard.length() < 13)
                showShortToast(R.string.increment_bank_card_input);
            else if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else
                sendSignMessage(mName, mIDCard, bankCard, mobile);
        }

        public void onCliclAggrement() {

        }

        public void onCliclOpen() {
            String bankCard = mBinding.etBankCard.getText().toString();
            String mobile = mBinding.etMobile.getText().toString();
            String verifyCode = mBinding.etVerifyCode.getText().toString();

            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mIDCard))
                showShortToast(R.string.increment_identity_information_error);
            else if (TextUtils.isEmpty(bankCard))
                showShortToast(R.string.increment_bank_card_input);
            else if (bankCard.length() < 13)
                showShortToast(R.string.increment_bank_card_input);
            else if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (verifyCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else if (!mBinding.checkboxAgree.isChecked())
                showShortToast(R.string.increment_aggrement_message);
            else
                sign(mName, mIDCard, bankCard, mobile, verifyCode);
        }

    }

    @Override
    public void onBackPressed() {
        hiddenKeyBoard();

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mWithholdMessagePopUpWindow && mWithholdMessagePopUpWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

}
