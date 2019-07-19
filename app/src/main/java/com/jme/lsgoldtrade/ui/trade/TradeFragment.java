package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.FragmentTradeBinding;
import com.jme.lsgoldtrade.domain.VerifyIdCardVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.SpanUtils;
import com.orhanobut.logger.Logger;

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

    private String url = "";
    private String name;
    private String idCard;
    private VerifyIdCardVo value;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentTradeBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_toolbar_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (User.getInstance().isLogin() && !TextUtils.isEmpty(User.getInstance().getAccountID())) {
            mBinding.layoutLogin.setVisibility(View.VISIBLE);
            mBinding.layoutNoLogin.setVisibility(View.GONE);
            mAdapter = new TabViewPagerAdapter(getChildFragmentManager());
            initInfoTabs();

            sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
        } else {
            mBinding.layoutLogin.setVisibility(View.GONE);
            mBinding.layoutNoLogin.setVisibility(View.VISIBLE);
            mBinding.kaihujiaocheng.setText(new SpanUtils(mContext)
                    .append("开户有疑问?来看看")
                    .setForegroundColor(getResources().getColor(R.color.color_000))
                    .append("开户教程")
                    .setForegroundColor(getResources().getColor(R.color.color_0080ff))
                    .create());
//            PicassoUtils.getInstance().loadImg(mContext, url, mBinding.imgBanner);
        }
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
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "WhetherIdCard":
                if (head.isSuccess()) {
                    try {
                        value = (VerifyIdCardVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }
                }
                break;
        }
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

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_CHICANG_FRAGMENT:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(0));

                    break;
                case Constants.RxBusConst.RXBUS_TRADEFRAGMENT:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
                case Constants.RxBusConst.RXBUS_CHEDAN_FRAGMENT:
                    mActivity.runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(2));

                    break;
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).onHiddenChanged(hidden);
    }

    public class ClickHandlers {

        public void onClickNews() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                        .navigation();
        }

        public void onClickKaiHu() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NAMECARDCHECK)
                        .withString("tag", "1")
                        .navigation();
        }

        public void onClickKaiHuJiaoCheng() {
            String url = "http://www.taijs.com/upload/glht/khjc.html";
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "开户教程")
                    .withString("url", url)
                    .navigation();
        }

        public void onClickBangDing() {
            if (null == mUser || !mUser.isLogin()) {
                showNeedLoginDialog();
            } else {
                if (TextUtils.isEmpty(value.getName())) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.NAMECARDCHECK)
                            .withString("tag", "2")
                            .navigation();
                } else {
                    String name = value.getName();
                    String idCard = value.getIdCard();
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.BINDUSERNAME)
                            .withString("name", name)
                            .withString("card", idCard)
                            .navigation();
                }
            }
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
