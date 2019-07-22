package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxBinding;
import com.jme.lsgoldtrade.domain.IsSubscribeVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
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

    private ArrayList<JMEBaseFragment> listFragment = new ArrayList<>();
    private TradingBoxAdapter adapter;
    private TradingBoxFragment tradingBoxFragment;
    private List<TradingBoxVo.HistoryListVoListBean> historyListVoList;
    private int subscriberCount;
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

        setRightNavigation("", R.mipmap.function, 0, () -> TradeBoxFunctionUtils.popup(this, ""));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getStudyData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserSubscribe();
    }

    private void getStudyData() {
        sendRequest(ManagementService.getInstance().tradingBox, new HashMap<>(), false);
    }

    private void getUserSubscribe() {
        sendRequest(ManagementService.getInstance().isSubscribe, new HashMap<>(), false);
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
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "TradingBox":
                if (head.isSuccess()) {
                    TradingBoxVo value;

                    try {
                        value = (TradingBoxVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }
                    subscriberCount = value.getSubscriberCount();
                    String periodName = value.getPeriodName();
                    String periodId = value.getPeriodId();
                    mBinding.tvSubscribeNumber.setText("已有" + subscriberCount + "位用户使用");
                    historyListVoList = value.getHistoryListVoList();

                    mBinding.tvSubscribe.setText("订 阅");
                    mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.white));
                    mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));

                    for (int i = 0; i < historyListVoList.size(); i++) {
                        tradingBoxFragment = new TradingBoxFragment();
                        listFragment.add(tradingBoxFragment);
                    }

                    adapter = new TradingBoxAdapter(getSupportFragmentManager(), this, historyListVoList, listFragment);
                    mBinding.viewpager.setAdapter(adapter);
                    mBinding.viewpager.setOffscreenPageLimit(2);
                    if (historyListVoList != null && historyListVoList.size() <= 1) {
                        mBinding.btnPrevious.setVisibility(View.GONE);
                        mBinding.btnNext.setVisibility(View.GONE);
                    } else {
                        mBinding.btnPrevious.setVisibility(View.VISIBLE);
                        mBinding.btnNext.setVisibility(View.VISIBLE);
                    }
                    isBuy(0);

                    mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            isBuy(i);
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });
                } else {
                    AppManager.getAppManager().finishActivity();
                }
                break;
            case "IsSubscribe":
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
                        mBinding.tvSubscribeNumber.setText("已有" + list.size() + "位用户使用");
                        mBinding.tvSubscribe.setText("已订阅");
                        mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.color_blue_deep));
                        mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_stroke));
                    } else {
                        mBinding.tvSubscribe.setText("订 阅");
                    }
                }
                break;
            case "Subscribe":
                if (head.isSuccess()) {
                    subscriberCount += 1;
                    mBinding.tvSubscribeNumber.setText("已有" + subscriberCount + "位用户使用");
                    mBinding.tvSubscribe.setText("已订阅");
                    mBinding.tvSubscribe.setTextColor(getResources().getColor(R.color.color_blue_deep));
                    mBinding.tvSubscribe.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_stroke));
                }
                break;
        }
    }

    private void isBuy(int i) {
        int closeTime = historyListVoList.get(i).getCloseTime();
        if (closeTime <= 0) {
            mBinding.tvBuy.setEnabled(false);
            mBinding.tvBuy.setBackgroundResource(R.color.color_white);
        } else {
            mBinding.tvBuy.setEnabled(true);
            mBinding.tvBuy.setBackgroundResource(R.color.color_blue_deep);
        }
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            if (list == null || list.isEmpty()) {
                sendRequest(ManagementService.getInstance().setSubscribe, new HashMap<>(), false);
            }
        }

        public void onClickLeft() {
            int currentItem = mBinding.viewpager.getCurrentItem();
            if (currentItem != 0) {
                mBinding.viewpager.setCurrentItem(currentItem - 1);
                tradingBoxFragment.setData(historyListVoList, currentItem - 1);
            }
        }

        public void onClickRight() {
            int currentItem = mBinding.viewpager.getCurrentItem();
            if (currentItem < historyListVoList.size() - 1) {
                mBinding.viewpager.setCurrentItem(currentItem + 1);
                tradingBoxFragment.setData(historyListVoList, currentItem + 1);
            }
        }

        public void onClickDetails() {
            if (tradingBoxFragment != null) {
                tradeId = tradingBoxFragment.getTradeId();
                int currentItem = mBinding.viewpager.getCurrentItem();
                tradeId = ((TradingBoxFragment) listFragment.get(currentItem)).getTradeId();

                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.TRADINGBOXDETAILS)
                        .withString("tradeId", tradeId)
                        .withString("type", "1")
                        .navigation();
            }
        }

        public void onClickBuy() {
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            } else {
                if (tradingBoxFragment != null) {
                    int currentItem = mBinding.viewpager.getCurrentItem();
                    tradeId = ((TradingBoxFragment) listFragment.get(currentItem)).getTradeId();
                    direction = ((TradingBoxFragment) listFragment.get(currentItem)).getDirection();

                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.PLACEORDER)
                            .withString("type", direction)
                            .withString("tradeId", tradeId)
                            .navigation();
                }
            }
        }
    }
}
