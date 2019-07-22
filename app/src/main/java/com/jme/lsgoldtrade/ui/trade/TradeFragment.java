package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentTradeBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;

import rx.Subscription;

/**
 * 交易
 */
public class TradeFragment extends JMEBaseFragment {

    private FragmentTradeBinding mBinding;

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
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null == mUser || !mUser.isLogin()) {
            setUnLoginLayout();
        } else {
            if (TextUtils.isEmpty(mUser.getAccountID()))
                setUnLoginLayout();
            else
                setLoginLayout();
        }
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_CHICANG_FRAGMENT:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(0));

                    break;
                case Constants.RxBusConst.RXBUS_TRADE:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
                case Constants.RxBusConst.RXBUS_CHEDAN_FRAGMENT:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(2));

                    break;
            }
        });
    }

    private void setUnLoginLayout() {
        mBinding.layoutLogin.setVisibility(View.GONE);
        mBinding.layoutNoLogin.setVisibility(View.VISIBLE);

        setCourseLayout(mContext.getResources().getString(R.string.trade_open_account_course));
    }

    private void setLoginLayout() {
        mBinding.layoutLogin.setVisibility(View.VISIBLE);
        mBinding.layoutNoLogin.setVisibility(View.GONE);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        initInfoTabs();
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
        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.trade_hold_position);
        mTabTitles[1] = mContext.getResources().getString(R.string.market_declaration_form);
        mTabTitles[2] = mContext.getResources().getString(R.string.trade_cancel_order);
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
        mBinding.tabViewpager.setOffscreenPageLimit(4);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);

        initRxBus();
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
                                .navigation();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickNews() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.NEWSCENTERACTIVITY).navigation();
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
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.AUTHENTICATION)
                        .withString("Type", "1")
                        .navigation();
        }

        public void onClickBind() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
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
