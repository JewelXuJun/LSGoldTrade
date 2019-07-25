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
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxDetailBinding;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryItemVo;
import com.jme.lsgoldtrade.util.TradeBoxFunctionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易匣子详情
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXDETAIL)
public class TradingBoxDetailActivity extends JMEBaseActivity {

    private ActivityTradingBoxDetailBinding mBinding;

    private String mType;
    private String mTradeId;

    private List<TradingBoxHistoryItemVo.HistoryListVoListBean> mHistoryListVoListBeanList;
    private ArrayList<TradingBoxHistoryFragment> mFragmentList = new ArrayList<>();

    private TradingBoxHistoryAdapter mAdapter;

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

        mType = getIntent().getStringExtra("Type");

        if (mType.equals("1")) {
            mTradeId = getIntent().getStringExtra("Value");

            initToolbar(String.format(getString(R.string.trading_box_number), getIntent().getStringExtra("PeriodName")), true);

            setRightNavigation("", R.mipmap.ic_more, 0, () ->
                    TradeBoxFunctionUtils.show(this, Constants.HttpConst.URL_TRADINGBOXINFO + mTradeId,
                    String.format(getString(R.string.trading_box_number), getIntent().getStringExtra("PeriodName")),
                    getString(R.string.trading_box_share_content), mBinding.layout.getId()));

            initViewPagerSimple();
        } else if (mType.equals("2")) {
            TradingBoxHistoryItemVo tradingBoxHistoryItemVo = new Gson().fromJson(getIntent().getStringExtra("Value"), new TypeToken<TradingBoxHistoryItemVo>() {
            }.getType());

            if (null == tradingBoxHistoryItemVo)
                return;

            initToolbar(String.format(getString(R.string.trading_box_number), tradingBoxHistoryItemVo.getPeriodName()), true);

            mHistoryListVoListBeanList = tradingBoxHistoryItemVo.getHistoryListVoList();

            if (null == mHistoryListVoListBeanList || 0 == mHistoryListVoListBeanList.size())
                return;

            initViewPager();
        }
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

    private void initViewPagerSimple() {
        mFragmentList.add(new TradingBoxHistoryFragment());

        mAdapter = new TradingBoxHistoryAdapter(getSupportFragmentManager());

        mBinding.viewpager.removeAllViewsInLayout();
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(1);

        mBinding.btnPrevious.setVisibility(View.GONE);
        mBinding.btnNext.setVisibility(View.GONE);
    }

    private void initViewPager() {
        for (int i = 0; i < mHistoryListVoListBeanList.size(); i++) {
            mFragmentList.add(new TradingBoxHistoryFragment());
        }

        mAdapter = new TradingBoxHistoryAdapter(getSupportFragmentManager());

        mBinding.viewpager.removeAllViewsInLayout();
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(2);

        mBinding.btnPrevious.setVisibility(mHistoryListVoListBeanList.size() == 1 ? View.GONE : View.VISIBLE);
        mBinding.btnNext.setVisibility(mHistoryListVoListBeanList.size() == 1 ? View.GONE : View.VISIBLE);

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
        if (mHistoryListVoListBeanList.size() > 1) {
            int currentPage = mBinding.viewpager.getCurrentItem();

            mBinding.btnPrevious.setAlpha(currentPage == 0 ? 0.5f : 1.0f);
            mBinding.btnNext.setAlpha(currentPage == mFragmentList.size() - 1 ? 0.5f : 1.0f);
        }
    }

    public class ClickHandlers {

        public void onClickPrevious() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem != 0)
                mBinding.viewpager.setCurrentItem(currentItem - 1, true);
        }

        public void onClickNext() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem < mHistoryListVoListBeanList.size() - 1)
                mBinding.viewpager.setCurrentItem(currentItem + 1, true);
        }

    }

    public class TradingBoxHistoryAdapter extends FragmentPagerAdapter {

        public TradingBoxHistoryAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            TradingBoxHistoryFragment tradingBoxHistoryFragment = mFragmentList.get(position);
            tradingBoxHistoryFragment.setData(mType.equals("1") ? mTradeId : mHistoryListVoListBeanList.get(position).getTradeId(), mType);

            return tradingBoxHistoryFragment;
        }

        @Override
        public int getCount() {
            if (mType.equals("1"))
                return 1;
            else
                return null == mHistoryListVoListBeanList ? 0 : mHistoryListVoListBeanList.size();
        }

    }

}
