package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityFeedbackBinding;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.FEEDBACK)
public class FeedBackActivity extends JMEBaseActivity {

    private ActivityFeedbackBinding mBinding;

    private int mMaxLength = AppConfig.MaxLength;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.personal_feedback, true);

        setRightNavigation();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvSurplus.setText(getString(R.string.personal_feedback_surplus) + mMaxLength + getString(R.string.personal_feedback_word));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.etSuggestion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});

        mBinding.etSuggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUIWithValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = mBinding.etSuggestion.getSelectionStart() - 1;

                if (index > 0) {
                    if (isEmojiCharacter(s.charAt(index))) {
                        Editable edit = mBinding.etSuggestion.getText();
                        edit.delete(index, index + 1);

                        showShortToast(R.string.personal_feedback_emoji);
                    }
                }
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityFeedbackBinding) mBindingUtil;
    }

    private void setRightNavigation() {
        setRightNavigation(getString(R.string.personal_feedback_send), 0, R.style.ToolbarThemeBlue, () -> {
            String suggest = mBinding.etSuggestion.getText().toString().trim();

            if (TextUtils.isEmpty(suggest))
                showShortToast(R.string.setting_feedback_empty);
            else
                feedBack(suggest);
        });
    }

    private void updateUIWithValidation() {
        mBinding.tvSurplus.setText(getString(R.string.personal_feedback_surplus)
                + (mMaxLength - mBinding.etSuggestion.getText().toString().trim().length()) + getString(R.string.personal_feedback_word));
    }


    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000)
                && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    private void feedBack(String suggest) {
        HashMap<String, String> params = new HashMap<>();
        params.put("suggest", suggest);

        sendRequest(ManagementService.getInstance().feedback, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "FeedBack":
                if (head.isSuccess()) {
                    showShortToast(R.string.setting_feedback_success);

                    mBinding.etSuggestion.setText("");
                }

                break;
        }
    }
}
