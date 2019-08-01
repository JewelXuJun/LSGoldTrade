package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxBinding;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.TradingBoxDataInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.ui.trade.TradeMessagePopUpWindow;
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
    private TradeMessagePopUpWindow mTradeMessagePopUpWindow;

    private boolean bSubscribeFlag = false;
    private String mPeriodName;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("", true);

        setRightNavigation("", R.mipmap.ic_more, 0, () ->
                TradeBoxFunctionUtils.show(this, Constants.HttpConst.URL_TRADINGBOX, String.format(getString(R.string.trading_box_number), mPeriodName),
                        getString(R.string.trading_box_share_content), mBinding.tvBuy.getId()));

        mTradeMessagePopUpWindow = new TradeMessagePopUpWindow(mContext);
        mTradeMessagePopUpWindow.setOutsideTouchable(true);
        mTradeMessagePopUpWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        getTradeBoxHomedataInfo();
        getListExt();
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

    private void getListExt() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false);
    }

    private void getTradeBoxHomedataInfo() {
        sendRequest(ManagementService.getInstance().tradeBoxHomedataInfo, new HashMap<>(), true);
    }

    private void setAppSubscribe() {
        sendRequest(ManagementService.getInstance().setAppSubscribe, new HashMap<>(), true);
    }

    private void getUserAddedServicesStatus() {
        sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), true);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetListExt":
                if (head.isSuccess()) {
                    SubscribeStateVo subscribeStateVo;

                    try {
                        subscribeStateVo = (SubscribeStateVo) response;
                    } catch (Exception e) {
                        subscribeStateVo = null;

                        e.printStackTrace();
                    }

                    if (null == subscribeStateVo)
                        return;

                    List<SubscribeStateVo.SubscribeBean> subscribeBeanList = subscribeStateVo.getList();

                    if (null == subscribeBeanList || 0 == subscribeBeanList.size())
                        bSubscribeFlag = false;
                    else
                        bSubscribeFlag = true;

                    mBinding.tvSubscribe.setText(bSubscribeFlag ? R.string.trading_box_subscribed : R.string.trading_box_subscribe);
                    mBinding.tvSubscribe.setTextColor(bSubscribeFlag ? ContextCompat.getColor(this, R.color.color_blue_deep)
                            : ContextCompat.getColor(this, R.color.white));
                    mBinding.tvSubscribe.setBackground(bSubscribeFlag ? ContextCompat.getDrawable(this, R.drawable.bg_btn_blue_stroke)
                            : ContextCompat.getDrawable(this, R.drawable.bg_btn_blue_solid));
                }

                break;
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

                    mPeriodName = tradingBoxDataInfoVo.getPeriodName();

                    initToolbar(String.format(getString(R.string.trading_box_number), mPeriodName), true);

                    mBinding.tvSubscribeNumber.setText(String.format(getString(R.string.trading_box_subscribe_number),
                            String.valueOf(tradingBoxDataInfoVo.getSubscriberCount())));

                    mHistoryVoBeanList = tradingBoxDataInfoVo.getHistoryListVoList();

                    if (null == mHistoryVoBeanList || 0 == mHistoryVoBeanList.size())
                        return;

                    initViewPager();
                }

                break;
            case "SetAppSubscribe":
                if (head.isSuccess())
                    getListExt();

                break;
            case "GetUserAddedServicesStatus":
                String incrementState;

                if (null == response)
                    incrementState = "";
                else
                    incrementState = response.toString();

                if (incrementState.equals("T")) {
                    getStatus();
                } else {
                    if (null != mTradeMessagePopUpWindow && !mTradeMessagePopUpWindow.isShowing()) {
                        mTradeMessagePopUpWindow.setData(mContext.getResources().getString(R.string.trade_increment_error),
                                mContext.getResources().getString(R.string.trade_increment_goto_open),
                                (view) -> {
                                    ARouter.getInstance().build(Constants.ARouterUriConst.VALUEADDEDSERVICE).navigation();

                                    mTradeMessagePopUpWindow.dismiss();
                                });
                        mTradeMessagePopUpWindow.showAtLocation(mBinding.tvBuy, Gravity.CENTER, 0, 0);
                    }
                }

                break;
            case "GetStatus":
                if (head.isSuccess()) {
                    String status;

                    if (null == response)
                        status = "";
                    else
                        status = response.toString();

                    if (status.equals("1")) {
                        if (null != mTradeMessagePopUpWindow && !mTradeMessagePopUpWindow.isShowing()) {
                            mTradeMessagePopUpWindow.setData(mContext.getResources().getString(R.string.trade_account_error),
                                    mContext.getResources().getString(R.string.trade_account_goto_recharge),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.RECHARGE).navigation();

                                        mTradeMessagePopUpWindow.dismiss();
                                    });
                            mTradeMessagePopUpWindow.showAtLocation(mBinding.tvBuy, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        if (null == mHistoryVoBeanList || 0 == mHistoryVoBeanList.size())
                            return;

                        TradingBoxDataInfoVo.HistoryVoBean historyVoBean = mHistoryVoBeanList.get(mBinding.viewpager.getCurrentItem());

                        if (null == historyVoBean)
                            return;

                        String tradeId = historyVoBean.getTradeId();
                        String direction = historyVoBean.getDirection();

                        if (TextUtils.isEmpty(tradeId) || TextUtils.isEmpty(direction))
                            return;

                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.PLACEORDER)
                                .withString("Type", "Place")
                                .withString("Direction", direction)
                                .withString("TradeId", tradeId)
                                .navigation();
                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            if (!bSubscribeFlag)
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
            if (null == mHistoryVoBeanList || 0 == mHistoryVoBeanList.size())
                return;

            TradingBoxDataInfoVo.HistoryVoBean historyVoBean = mHistoryVoBeanList.get(mBinding.viewpager.getCurrentItem());

            if (null == historyVoBean)
                return;

            String tradeId = historyVoBean.getTradeId();

            if (TextUtils.isEmpty(tradeId))
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.TRADINGBOXDETAIL)
                    .withString("Value", tradeId)
                    .withString("PeriodName", mPeriodName)
                    .withString("Type", "1")
                    .navigation();
        }

        public void onClickBuy() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                getUserAddedServicesStatus();
        }
    }

}
