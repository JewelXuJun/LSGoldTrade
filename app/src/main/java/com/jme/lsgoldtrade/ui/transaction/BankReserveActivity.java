package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBankReserveBinding;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.IntentUtils;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.BANKRESERVE)
public class BankReserveActivity extends JMEBaseActivity {

    private ActivityBankReserveBinding mBinding;

    private boolean bFlag = false;

    private TextWatcher mWatcher;
    private JMECountDownTimer mCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bank_reserve;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_transfer_bank_reserve, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        setFileValue();

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getResources().getString(R.string.transaction_get_verification_code));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWatcher = validationTextWatcher();

        mBinding.etIcbcElectronicCard.addTextChangedListener(mWatcher);
        mBinding.etMobile.addTextChangedListener(mWatcher);
        mBinding.etVerificationCode.addTextChangedListener(mWatcher);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBankReserveBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setFileValue() {
        String fileValue = getResources().getString(R.string.transaction_transfer_icbc_electronic_card_file_message);
        SpannableString spannableString = new SpannableString(fileValue);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_blue)),
                fileValue.length() - 5, fileValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), fileValue.length() - 5, fileValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBinding.tvIcbcElectronicCardFile.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvIcbcElectronicCardFile.setText(spannableString);
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(final Editable s) {
                mBinding.btnSave.setEnabled(mBinding.etIcbcElectronicCard.length() > 0 && mBinding.etMobile.length() > 0
                        && mBinding.etVerificationCode.length() > 0);
            }
        };
    }

    private void sendPassCode(String mobile) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        sendRequest(TradeService.getInstance().sendPassCode, params, true);
    }

    private void keepInfoIntoList(String icbcElectronicCard, String mobile, String verificationCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("electronicAccounts", icbcElectronicCard);
        params.put("mobile", mobile);
        params.put("smsCode", verificationCode);

        sendRequest(TradeService.getInstance().keepInfoIntoList, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "SendPassCode":
                if (head.isSuccess()) {
                    bFlag = true;

                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "KeepInfoIntoList":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_transfer_bank_save_success);

                    if (null != mUser && null != mUser.getCurrentUser()) {
                        mUser.getCurrentUser().setCardType("2");
                        mUser.getCurrentUser().setReserveFlag("Y");
                    }

                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickGetVerificationCode() {
            String etIcbcElectronicCard = mBinding.etIcbcElectronicCard.getText().toString();
            String mobile = mBinding.etMobile.getText().toString();

            if (TextUtils.isEmpty(etIcbcElectronicCard))
                showShortToast(R.string.transaction_transfer_icbc_electronic_card_input);
            else if (TextUtils.isEmpty(mobile) || mobile.length() < 11)
                showShortToast(R.string.transaction_transfer_icbc_electronic_card_mobile_input);
            else
                sendPassCode(mobile);
        }

        public void onClickSave() {
            String mobile = mBinding.etMobile.getText().toString();
            String verificationCode = mBinding.etVerificationCode.getText().toString();

            if (mobile.length() < 11)
                showShortToast(R.string.transaction_transfer_icbc_electronic_card_mobile_input);
            else if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (verificationCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else
                keepInfoIntoList(mBinding.etIcbcElectronicCard.getText().toString(), mobile, verificationCode);
        }

    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            if (IntentUtils.isWeChatAvilible(BankReserveActivity.this))
                IntentUtils.intentICBCSmall(BankReserveActivity.this);
            else
                showShortToast(R.string.text_wechat_uninstalled);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

}
