package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxBinding;
import com.jme.lsgoldtrade.domain.IsSubscribeVo;
import com.jme.lsgoldtrade.domain.TradingBoxDataInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.TradeBoxFunctionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 交易匣子
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOX)
public class TradingBoxActivity extends JMEBaseActivity {

    private ActivityTradingBoxBinding mBinding;

    private List<TradingBoxDataInfoVo.HistoryVoBean> mHistoryVoBeanList;
    private ArrayList<TradingBoxFragment> mListFragment = new ArrayList<>();

    private TradingBoxAdapter mAdapter;

    private String tradeId = "";
    private String direction;
    private List<IsSubscribeVo.ListBean> list;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("", true);

        setRightNavigation("", R.mipmap.ic_more, 0, () -> TradeBoxFunctionUtils.popup(this, ""));
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
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTradeBoxHomedataInfo(true);
        getUserSubscribe();
    }

    private void initViewPager() {
        for (int i = 0; i < mHistoryVoBeanList.size(); i++) {
            mListFragment.add(new TradingBoxFragment());
        }

        mAdapter = new TradingBoxAdapter(getSupportFragmentManager(), mHistoryVoBeanList, mListFragment);

        mBinding.viewpager.removeAllViewsInLayout();
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(2);

        mBinding.btnPrevious.setVisibility(mHistoryVoBeanList.size() == 1 ? View.GONE : View.VISIBLE);
        mBinding.btnNext.setVisibility(mHistoryVoBeanList.size() == 1 ? View.GONE : View.VISIBLE);

        isCanBuy(0);

        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                isCanBuy(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void isCanBuy(int position) {
        if (mHistoryVoBeanList.size() > 1) {
            int currentPage = mBinding.viewpager.getCurrentItem();

            mBinding.btnPrevious.setAlpha(currentPage == 0 ? 0.5f : 1.0f);
            mBinding.btnNext.setAlpha(currentPage == mListFragment.size() - 1 ? 0.5f : 1.0f);
        }

        long closeTime = mHistoryVoBeanList.get(position).getCloseTime();

        mBinding.tvBuy.setEnabled(closeTime <= 0 ? false : true);
        mBinding.tvBuy.setBackgroundResource(closeTime <= 0 ? R.color.color_text_gray_hint : R.color.color_blue_deep);
    }

    private void getTradeBoxHomedataInfo(boolean enable) {
        sendRequest(ManagementService.getInstance().tradeBoxHomedataInfo, new HashMap<>(), enable);
    }

    private void getUserSubscribe() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false);
    }

    private void setAppSubscribe() {
        sendRequest(ManagementService.getInstance().setAppSubscribe, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "TradeBoxHomedataInfo":
                if (head.isSuccess()) {
                    TradingBoxDataInfoVo tradingBoxDataInfoVo;

                    try {
                        tradingBoxDataInfoVo = (TradingBoxDataInfoVo) response;
                    } catch (Exception e) {
                        tradingBoxDataInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxDataInfoVo)
                        return;

                    initToolbar(String.format(getString(R.string.trading_box_number), tradingBoxDataInfoVo.getPeriodName()), true);

                    mBinding.tvSubscribeNumber.setText(String.format(getString(R.string.trading_box_subscribe_number),
                            String.valueOf(tradingBoxDataInfoVo.getSubscriberCount())));

                    mHistoryVoBeanList = tradingBoxDataInfoVo.getHistoryListVoList();

                    if (null == mHistoryVoBeanList || 0 == mHistoryVoBeanList.size())
                        return;

                    initViewPager();
                }

                break;
            case "GetListExt":
                if (head.isSuccess()) {
                    IsSubscribeVo value;

                    try {
                        value = (IsSubscribeVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }
                    list = value.getList();
                    if (list != null && list.size() > 0) {
                        mBinding.tvSubscribe.setText("已订阅");
                        mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.color_blue_deep));
                        mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_stroke));
                    } else {
                        mBinding.tvSubscribe.setText("订 阅");
                        mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.white));
                        mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));
                    }
                }
                break;
            case "SetAppSubscribe":
                if (head.isSuccess()) {
                    getTradeBoxHomedataInfo(false);
                    getUserSubscribe();

                    mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.color_blue_deep));
                    mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_stroke));
                }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            if (list == null || list.isEmpty())
                setAppSubscribe();
        }

        public void onClickPrevious() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem != 0)
                mBinding.viewpager.setCurrentItem(currentItem - 1, true);
        }

        public void onClickNext() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem < mHistoryVoBeanList.size() - 1)
                mBinding.viewpager.setCurrentItem(currentItem + 1, true);
        }

        public void onClickDetails() {
            /*if (tradingBoxFragment != null) {
                tradeId = tradingBoxFragment.getTradeId();
                int currentItem = mBinding.viewpager.getCurrentItem();
                tradeId = ((TradingBoxFragment) listFragment.get(currentItem)).getTradeId();

                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.TRADINGBOXDETAILS)
                        .withString("tradeId", tradeId)
                        .withString("type", "1")
                        .navigation();
            }*/
        }

        public void onClickBuy() {
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            } else {
                /*if (tradingBoxFragment != null) {
                    int currentItem = mBinding.viewpager.getCurrentItem();
                    tradeId = ((TradingBoxFragment) listFragment.get(currentItem)).getTradeId();
                    direction = ((TradingBoxFragment) listFragment.get(currentItem)).getDirection();

                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.PLACEORDER)
                            .withString("type", direction)
                            .withString("tradeId", tradeId)
                            .navigation();
                }*/
            }
        }
    }
}
