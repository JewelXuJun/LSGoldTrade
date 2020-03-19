package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

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
import com.jme.lsgoldtrade.view.IncrementAlertDialog;
import com.jme.lsgoldtrade.view.WithholdMessagePopUpWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

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
    private boolean isChangeBank = false;
    private boolean isBindAccount = false;
    private IncrementAlertDialog mWithholdAlertDialog;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withhold_contract;
    }

    @Override
    protected void initView() {
        super.initView();


        isChangeBank = getIntent().getBooleanExtra("isChangeBank",false);
        isBindAccount = getIntent().getBooleanExtra("isBindAccount",false);
        if(isChangeBank) {
            initToolbar(R.string.increment_account_withhold_change_bank, true);
            mBinding.btnWithholdContractCommit.setText("变更");
            mBinding.tvWithholdContractDescribe.setVisibility(View.INVISIBLE);
            mBinding.llWithholdSelectBank.setVisibility(View.VISIBLE);
            mBinding.checkboxAgree.setChecked(true);
            mBinding.etBankCard.setHint(R.string.increment_account_withhold_contract_bankcard_hint);
        }else{
            initToolbar(R.string.increment_account_withhold_contract, true);
            mBinding.btnWithholdContractCommit.setText("立即开通");
            mBinding.tvWithholdContractDescribe.setVisibility(View.VISIBLE);
            mBinding.llWithholdSelectBank.setVisibility(View.GONE);
            mBinding.checkboxAgree.setChecked(false);
            mBinding.etBankCard.setHint(R.string.increment_account_withhold_contract_bankcard_gh_hint);
        }

        setAggrementMessage();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mResource = getIntent().getStringExtra("Resource");
        String bankName = getIntent().getStringExtra("bankName");
        if(!TextUtils.isEmpty(bankName)){
            mBinding.tvBankName.setText(bankName);
        }

        mBankPopUpWindow = new BankPopUpWindow(this);

        mWithholdMessagePopUpWindow = new WithholdMessagePopUpWindow(this);
        mWithholdMessagePopUpWindow.setOutsideTouchable(false);
        mWithholdMessagePopUpWindow.setFocusable(false);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.transaction_get_verification_code));

        getWhetherIdCard();


//        getBanks("");
    }

    @Override
    protected void initListener() {
        super.initListener();

//        mBinding.etBankCard.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence value, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence value, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String bankCard = editable.toString();
//
//                if (TextUtils.isEmpty(bankCard)) {
//                    mBinding.tvBankName.setText("");
//                } else {
//                    int length = bankCard.length();
//
//                    if (length >= 6)
//                        getBanks(bankCard);
//                    else if (length < 6)
//                        mBinding.tvBankName.setText("");
//                }
//            }
//        });
        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityWithholdContractBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_SELECT_BANK_CARD_SUCCESS:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    mBinding.tvBankName.setText(object.toString());
                    break;
            }
        });
    }

    private void setAggrementMessage() {
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.increment_aggrement));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                12, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), 12, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(10, true), 12, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvAggrement.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvAggrement.setText(spannableString);
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
        params.put("acBankName",mBinding.tvBankName.getText().toString());
        if(isBindAccount)
            params.put("openType", "1");
        else
            params.put("openType", "2");

        sendRequest(WithholdAccountService.getInstance().sign, params, true,false,false);
    }

    private void changeBankCard(String bankCard, String mobile, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();

        params.put("acNo", bankCard);
        params.put("mobile", mobile);
        params.put("validateCode", verifyCode);
        params.put("acBankName",mBinding.tvBankName.getText().toString());

        sendRequest(WithholdAccountService.getInstance().changeSignBankCard, params, true,false,false);
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

                    mBinding.tvName.setText(StringUtils.formatName(mName));
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
//                                    mUser.getCurrentUser().setIsSign("Y");
                                    mWithholdMessagePopUpWindow.dismiss();

                                    if (mResource.equals("Trade")) {
                                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                                    }

                                    finish();
                                });
                        mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvName, Gravity.CENTER, 0, 0);
                    }
                }else{
                    if("1001".equals(head.getCode())) {
                        showWithholdAlertDialog("请您确认填写的信息是否准确，稍后再尝试。", (v) -> {
                            mBinding.llWithholdSelectBank.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(mBinding.tvBankName.getText().toString())) {
                                mBinding.tvBankName.setText("工商银行");
                                mBinding.etBankCard.setHint(R.string.increment_account_withhold_contract_bankcard_hint);
                            }
                            mWithholdAlertDialog.dismiss();
                        });
                    }else{
                        showShortToast(head.getMsg());
                    }
                }

                break;
            case "changeSignBankCard":
                if (head.isSuccess()) {
//                    showShortToast(R.string.change_bind_bank_toast);
                    showWithholdAlertDialog("变更成功",(v)->{
                        mWithholdAlertDialog.dismiss();
                        finish();
                    });
                }else{
                    if("1001".equals(head.getCode())) {
                        showWithholdAlertDialog("请您确认填写的信息是否准确，稍后再尝试。", (v) -> {
                            mWithholdAlertDialog.dismiss();
                        });
                    }else{
                        showShortToast(head.getMsg());
                    }
                }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickBankList() {
            hiddenKeyBoard();

//            if (null != mBankPopUpWindow && !mBankPopUpWindow.isShowing()) {
//                mBankPopUpWindow.setData(mBankVoList);
//                mBankPopUpWindow.showAtLocation(mBinding.tvBankName, Gravity.CENTER, 0, 0);
//            }

            ARouter.getInstance().build(Constants.ARouterUriConst.SELECTBANKCARD)
                    .withString("bankName",mBinding.tvBankName.getText().toString())
                    .navigation();

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

        public void onCliclOpen() {
            String bankCard = mBinding.etBankCard.getText().toString();
            String mobile = mBinding.etMobile.getText().toString();
            String verifyCode = mBinding.etVerifyCode.getText().toString();

            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mIDCard)) {
                showShortToast(R.string.increment_identity_information_error);
            }else if (TextUtils.isEmpty(bankCard)) {
                showShortToast(R.string.increment_bank_card_input);
            }else if (bankCard.length() < 13){
                showShortToast(R.string.increment_bank_card_input);
            }else if (!ValueUtils.isPhoneNumber(mobile)) {
                showShortToast(R.string.login_mobile_error);
            }else if (!bFlag) {
                showShortToast(R.string.login_verification_code_unget);
            }else if (verifyCode.length() < 6) {
                showShortToast(R.string.login_verification_code_error);
            }else if (!mBinding.checkboxAgree.isChecked()) {
                showShortToast(R.string.increment_aggrement_message);
            }else {
                if(isChangeBank){
                    changeBankCard(bankCard, mobile, verifyCode);
                }else {
                    sign(mName, mIDCard, bankCard, mobile, verifyCode);
                }
            }
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

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mWithholdMessagePopUpWindow && mWithholdMessagePopUpWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.increment_aggrement_name))
                    .withString("url", Constants.HttpConst.URL_AGREEMENT_HUAXING + "?name=" + mName + "&cardNo=" + StringUtils.formatIDCardNumber(mIDCard)
                            + "&bankCard=" + mBinding.etBankCard.getText().toString() + "&bankName=" + mBinding.tvBankName.getText().toString())
                    .navigation();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    private void showWithholdAlertDialog(String tx, View.OnClickListener click) {
        if (isFinishing)
            return;

//        if (null == mWithholdAlertDialog)
            mWithholdAlertDialog = new IncrementAlertDialog(mContext,tx,click);

        if (!mWithholdAlertDialog.isShowing()) {
            mWithholdAlertDialog.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mWithholdAlertDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mWithholdAlertDialog.getWindow().setAttributes(lp);

        }
    }

}
