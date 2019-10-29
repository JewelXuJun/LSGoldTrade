package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.ihsg.patternlocker.DefaultIndicatorNormalCellView;
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView;
import com.github.ihsg.patternlocker.DefaultStyleDecorator;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGestureSettingBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.GESTURESETTING)
public class GestureSettingActivity extends JMEBaseActivity {

    private ActivityGestureSettingBinding mBinding;

    private String mPassword;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_gesture_setting;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_gesture_password_setting, true);

        initPatternIndicatorView();
        initPatternLockerView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(false);
        mConfirmSimplePopupwindow.setFocusable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(@NotNull PatternLockerView patternLockerView) {

            }

            @Override
            public void onChange(@NotNull PatternLockerView patternLockerView, @NotNull List<Integer> list) {

            }

            @Override
            public void onComplete(@NotNull PatternLockerView patternLockerView, @NotNull List<Integer> list) {
                if (null == list || list.size() < 4) {
                    mBinding.tvMessage.setText(R.string.security_gesture_password_error_point);
                    mBinding.tvMessage.setTextColor(ContextCompat.getColor(GestureSettingActivity.this, R.color.color_red));
                } else {
                    String value = list.toString();

                    if (TextUtils.isEmpty(mPassword)) {
                        mPassword = value;

                        mBinding.patternIndicatorView.updateState(list, false);
                        mBinding.tvMessage.setText(R.string.security_gesture_password_too);
                        mBinding.tvMessage.setTextColor(ContextCompat.getColor(GestureSettingActivity.this, R.color.color_text_normal));
                    } else {
                        if (mPassword.equalsIgnoreCase(value)) {
                            mBinding.tvMessage.setText("");
                            mBinding.tvMessage.setTextColor(ContextCompat.getColor(GestureSettingActivity.this, R.color.color_text_normal));
                            mBinding.patternLockerView.setEnableAutoClean(false);

                            setTradePassword(mPassword);
                        } else {
                            mBinding.tvMessage.setText(R.string.security_gesture_password_again);
                            mBinding.tvMessage.setTextColor(ContextCompat.getColor(GestureSettingActivity.this, R.color.color_red));
                        }
                    }

                }

            }

            @Override
            public void onClear(@NotNull PatternLockerView patternLockerView) {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityGestureSettingBinding) mBindingUtil;
    }

    private void initPatternIndicatorView() {
        DefaultStyleDecorator decorator = ((DefaultIndicatorNormalCellView) mBinding.patternIndicatorView.getNormalCellView()).getStyleDecorator();

        GestureNormalCellView normalCellView = new GestureNormalCellView();
        normalCellView.setDefaultColor(decorator.getNormalColor());
        normalCellView.setDivide(1.5f);

        GestureHitCellView hitCellView = new GestureHitCellView();
        hitCellView.setHitColor(decorator.getHitColor());
        hitCellView.setIndicatorView(true);

        mBinding.patternIndicatorView.setNormalCellView(normalCellView);
        mBinding.patternIndicatorView.setHitCellView(hitCellView);
        mBinding.patternIndicatorView.setLinkedLineView(new GestureLineCellView());
    }

    private void initPatternLockerView() {
        DefaultStyleDecorator decorator = ((DefaultLockerNormalCellView) mBinding.patternLockerView.getNormalCellView()).getStyleDecorator();

        GestureNormalCellView normalCellView = new GestureNormalCellView();
        normalCellView.setDefaultColor(decorator.getNormalColor());
        normalCellView.setDivide(2.5f);

        GestureHitCellView hitCellView = new GestureHitCellView();
        hitCellView.setHitColor(decorator.getHitColor());
        hitCellView.setIndicatorView(false);

        mBinding.patternLockerView.setNormalCellView(normalCellView);
        mBinding.patternLockerView.setHitCellView(hitCellView);
    }

    private void setTradePassword(String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("passwordType", "3");
        params.put("password", AESUtil.encryptString2Base64(password, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());

        sendRequest(ManagementService.getInstance().setTradePassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "SetTradePassword":
                if (head.isSuccess()) {
                    if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_setting_success_title),
                                getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mConfirmSimplePopupwindow.dismiss();

                                    finish();
                                });
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.patternLockerView, Gravity.CENTER, 0, 0);
                    }
                }

                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mConfirmSimplePopupwindow && mConfirmSimplePopupwindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }
}
