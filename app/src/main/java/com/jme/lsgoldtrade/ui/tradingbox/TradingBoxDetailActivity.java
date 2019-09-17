package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxDetailBinding;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.TradeBoxFunctionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.TRADINGBOXDETAIL)
public class TradingBoxDetailActivity extends JMEBaseActivity {

    private ActivityTradingBoxDetailBinding mBinding;

    private int mPosition;
    private String mPeriodName;
    private String mType;

    private List<TradingBoxVo.TradingBoxListVoBean> mTradingBoxListVoBeanList;
    private List<TradingBoxDetailFragment> mFragmentList = new ArrayList<>();

    private TradingBoxDetailAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mPosition = getIntent().getIntExtra("Position", -10000);
        mPeriodName = getIntent().getStringExtra("PeriodName");
        mType = getIntent().getStringExtra("Type");
        mTradingBoxListVoBeanList = new Gson().fromJson(getIntent().getStringExtra("Value"), new TypeToken<List<TradingBoxVo.TradingBoxListVoBean>>() {
        }.getType());

        initToolbar(String.format(getString(R.string.trading_box_number), mPeriodName), true);

        if (null == mTradingBoxListVoBeanList || 0 == mTradingBoxListVoBeanList.size())
            return;

        if (mType.equals("TradingBox"))
            setRightNavigation("", R.mipmap.ic_more, 0, () ->
                    TradeBoxFunctionUtils.show(this, Constants.HttpConst.URL_TRADINGBOXINFO
                                    + mTradingBoxListVoBeanList.get(mBinding.viewpager.getCurrentItem()).getTradeId(),
                            mTradingBoxListVoBeanList.get(mBinding.viewpager.getCurrentItem()).getMainTitle(),
                            mTradingBoxListVoBeanList.get(mBinding.viewpager.getCurrentItem()).getMainContent(), mBinding.layout.getId(), true));

        initViewPager();

        querySubscriberCount();
        getListExt();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxDetailBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initViewPager() {
        for (int i = 0; i < mTradingBoxListVoBeanList.size(); i++) {
            mFragmentList.add(new TradingBoxDetailFragment());
        }

        mAdapter = new TradingBoxDetailAdapter(getSupportFragmentManager());

        mBinding.viewpager.removeAllViewsInLayout();
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(2);
        mBinding.viewpager.setCurrentItem(mPosition);

        mBinding.btnPrevious.setVisibility(mTradingBoxListVoBeanList.size() == 1 ? View.GONE : View.VISIBLE);
        mBinding.btnNext.setVisibility(mTradingBoxListVoBeanList.size() == 1 ? View.GONE : View.VISIBLE);

        setChangeLayout();

        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                setChangeLayout();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setChangeLayout() {
        if (mTradingBoxListVoBeanList.size() > 1) {
            int currentPage = mBinding.viewpager.getCurrentItem();

            mBinding.btnPrevious.setAlpha(currentPage == 0 ? 0.5f : 1.0f);
            mBinding.btnNext.setAlpha(currentPage == mFragmentList.size() - 1 ? 0.5f : 1.0f);
        }
    }

    private void querySubscriberCount() {
        sendRequest(ManagementService.getInstance().querySubscriberCount, new HashMap<>(), false);
    }

    private void getListExt() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false);
    }

    private void setAppSubscribe() {
        sendRequest(ManagementService.getInstance().setAppSubscribe, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QuerySubscriberCount":
                if (head.isSuccess())
                    mBinding.tvSubscribeNumber.setText(String.format(getString(R.string.trading_box_subscribe_number), null == response ? 0 : (int) response));

                break;
            case "GetListExt":
                if (head.isSuccess()) {
                    SubscribeStateVo subscribeStateVo;

                    try {
                        subscribeStateVo = (SubscribeStateVo) response;
                    } catch (Exception e) {
                        subscribeStateVo = null;

                        e.printStackTrace();
                    }

                    if (null == subscribeStateVo) {
                        mBinding.tvUnSubscribe.setVisibility(View.GONE);
                        mBinding.tvSubscribe.setVisibility(View.GONE);
                    } else {
                        List<SubscribeStateVo.SubscribeBean> subscribeBeanList = subscribeStateVo.getList();

                        boolean subscribeFlag = null == subscribeBeanList || 0 == subscribeBeanList.size() ? false : true;

                        mBinding.tvUnSubscribe.setVisibility(subscribeFlag ? View.GONE : View.VISIBLE);
                        mBinding.tvSubscribe.setVisibility(subscribeFlag ? View.VISIBLE : View.GONE);
                    }
                }

                break;
            case "SetAppSubscribe":
                if (head.isSuccess())
                    getListExt();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            setAppSubscribe();
        }

        public void onClickPrevious() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem != 0)
                mBinding.viewpager.setCurrentItem(currentItem - 1, true);
        }

        public void onClickNext() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem < mTradingBoxListVoBeanList.size() - 1)
                mBinding.viewpager.setCurrentItem(currentItem + 1, true);
        }

    }

    public class TradingBoxDetailAdapter extends FragmentPagerAdapter {

        public TradingBoxDetailAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            TradingBoxDetailFragment tradingBoxDetailFragment = mFragmentList.get(position);
            tradingBoxDetailFragment.setData(mTradingBoxListVoBeanList.get(position).getTradeId(), mType);

            return tradingBoxDetailFragment;
        }

        @Override
        public int getCount() {
            if (mType.equals("1"))
                return 1;
            else
                return null == mTradingBoxListVoBeanList ? 0 : mTradingBoxListVoBeanList.size();
        }

    }

}
