package com.jme.lsgoldtrade.ui.trade;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentTradeBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;

import rx.Subscription;

/**
 * 交易
 */
public class TradeFragment extends JMEBaseFragment implements TabLayout.OnTabSelectedListener {

    private FragmentTradeBinding mBinding;

    private boolean bHidden = false;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private TabViewPagerAdapter mAdapter;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentTradeBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_blue_deep);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        initInfoTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.tablayout.addOnTabSelectedListener(this);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        bHidden = hidden;

        if (!bHidden)
            getUserPasswordSettingInfo();

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        setLayout();

        if (!bHidden)
            getUserPasswordSettingInfo();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_LOGIN_SUCCESS:
                    setLayout();

                    break;
                case Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(0));

                    break;
                case Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
                case Constants.RxBusConst.RXBUS_TRANSACTION_CANCEL_ORDER:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(2));

                    break;
            }
        });
    }

    private void setLayout() {
        if (null == mUser || !mUser.isLogin()) {
            setUnLoginLayout();
        } else {
            if (TextUtils.isEmpty(mUser.getAccountID()))
                setUnLoginLayout();
            else
                setLoginLayout();
        }
    }

    private void setUnLoginLayout() {
        mBinding.layoutLogin.setVisibility(View.GONE);
        mBinding.layoutNoLogin.setVisibility(View.VISIBLE);

        setCourseLayout(mContext.getResources().getString(R.string.trade_open_account_course));
    }

    private void setLoginLayout() {
        mBinding.layoutLogin.setVisibility(View.VISIBLE);
        mBinding.layoutNoLogin.setVisibility(View.GONE);
    }

    private void setCourseLayout(String value) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                value.length() - 4, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), value.length() - 4, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBinding.tvOpenAccountCourse.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvOpenAccountCourse.setText(spannableString);
    }

    private void initInfoTabs() {
        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.transaction_hold_positions);
        mTabTitles[1] = mContext.getResources().getString(R.string.market_declaration_form);
        mTabTitles[2] = mContext.getResources().getString(R.string.transaction_cancel_order);
        mTabTitles[3] = mContext.getResources().getString(R.string.trade_query);

        mFragmentArrays = new Fragment[4];
        mFragmentArrays[0] = new HoldPositionFragment();
        mFragmentArrays[1] = new DeclarationFormFragment();
        mFragmentArrays[2] = new CancelOrderFragment();
        mFragmentArrays[3] = new QueryFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    private void getWhetherIdCard() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), false, false, false);
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
                                .navigation();
                }

                break;
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasTimeout = passwordInfoVo.getHasTimeout();
                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();
                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();

                    if (TextUtils.isEmpty(hasSettingDigital) || hasSettingDigital.equals("N")) {
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING, null);
                    } else {
                        if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N"))
                            return;

                        int type = 1;

                        if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                            boolean isCanUseFingerPrint = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                FingerprintManager manager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);

                                if (manager.isHardwareDetected() && manager.hasEnrolledFingerprints())
                                    isCanUseFingerPrint = true;
                            }

                            if (isCanUseFingerPrint) {
                                type = 2;
                            } else {
                                if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y"))
                                    type = 3;
                                else
                                    type = 1;
                            }
                        } else if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y")) {
                            type = 3;
                        } else if (passwordInfoVo.getHasTimeout().equals("Y")) {
                            type = 1;
                        }

                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
                                .withInt("Type", type)
                                .navigation();
                    }
                }

                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class ClickHandlers {

        public void onClickSecuritySetting() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTSECURITY).navigation();
        }

        public void onClickBanner() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.trade_rule))
                    .withString("url", Constants.HttpConst.URL_TRADE_RULE)
                    .navigation();
        }

        public void onClickOpenAccountFree() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.AUTHENTICATION)
                        .withString("Type", "1")
                        .navigation();
        }

        public void onClickBind() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                getWhetherIdCard();
        }
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.trade_open_account_course_title))
                    .withString("url", Constants.HttpConst.URL_OPEN_ACCOUNT_COURSE)
                    .navigation();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    final class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
