package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketJudgmentBinding;
import com.jme.lsgoldtrade.domain.FenXiShiVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.EveningUpPopupWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 行情研判
 * 展示30分钟Au(T+D)
 */
@Route(path = Constants.ARouterUriConst.MARKETJUDGMENT)
public class MarketJudgmentActivity extends JMEBaseActivity {

    private ActivityMarketJudgmentBinding mBinding;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> mTabTitles = new ArrayList<>();

    private MarketJudgmentFragment hangQingYanPanFragment;

    private Subscription mRxbus;

    private HangQingYanPanAdapter adapter;

    private List<FenXiShiVo> fenXiShiVoList;

    private EveningUpPopupWindow mWindow;

    private String latestPrice;
    private String value;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_judgment;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityMarketJudgmentBinding) mBindingUtil;
        initToolbar("行情研判", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWindow = new EveningUpPopupWindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
        getDateFromNet();
        getPrice();
    }

    private void getPrice() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "Au(T+D)");
        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getDateFromNet() {
        sendRequest(ManagementService.getInstance().fenxishi, new HashMap<>(), false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "FenXiShiList":
                if (head.isSuccess()) {
                    try {
                        fenXiShiVoList = (List<FenXiShiVo>) response;
                    } catch (Exception e) {
                        fenXiShiVoList = null;

                        e.printStackTrace();
                    }
                    if (null == fenXiShiVoList || 0 == fenXiShiVoList.size())
                        return;

                    for (FenXiShiVo fenXiShiVo : fenXiShiVoList) {
                        if (null != fenXiShiVo) {
                            hangQingYanPanFragment = new MarketJudgmentFragment();
                            fragmentList.add(hangQingYanPanFragment);
                            mTabTitles.add(fenXiShiVo.getName());
                        }
                    }
                    adapter = new HangQingYanPanAdapter(getSupportFragmentManager());
                    initTabLayout();
                }
                break;
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {

                    List<TenSpeedVo> list;

                    try {
                        list = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    TenSpeedVo mTenSpeedVo = list.get(0);

                    if (null == mTenSpeedVo)
                        return;

                    latestPrice = mTenSpeedVo.getLatestPrice();
                }
                break;
            case "LimitOrder":
                if (head.isSuccess())
                    showShortToast(R.string.trade_success);
                else
                    showShortToast(head.getMsg());
                break;
            case "GetMaxTradeNum":
                if (head.isSuccess()) {
                    value = head.getValue();
                    showPopupWindow(latestPrice, "1", "0".equals(isMore) ? "1" : "2", "0", value);//多
                } else {
                    showShortToast(head.getMsg());
                }
                break;
        }
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(adapter);
        mBinding.tabViewpager.setOffscreenPageLimit(1);
        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRADE:
                    runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(1));

                    break;
            }
        });
    }

    final class HangQingYanPanAdapter extends FragmentPagerAdapter {

        public HangQingYanPanAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MarketJudgmentFragment fragment = (MarketJudgmentFragment) fragmentList.get(position);
            fragment.getanalystId(fenXiShiVoList.get(position).getId());
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
        }
    }

    private String isMore = "0";

    public class ClickHandlers {

        public void onClickBuyMore() {
            isMore = "0";
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
            else
                getMaxNum(isMore);
//                showPopupWindow(latestPrice, "1", "1", "0");
        }

        public void onClickSaleEmpty() {
            isMore = "1";
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
            else
                getMaxNum(isMore);
//                showPopupWindow(latestPrice, "1", "2", "0");
        }

        public void onClickBaoDan() {
            if (null == mUser || !mUser.isLogin()) {
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
            } else {
                AppConfig.Select_ContractId = "Au(T+D)";

                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, "Au(T+D)");

                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.MAIN)
                        .navigation();

                finish();
            }
        }
    }

    public void getMaxNum(String isMore) {
        HashMap<String, String> params = new HashMap<>();
        params.put("variety", "Au(T+D)");
        params.put("direction", isMore);

//        sendRequest(ManagementService.getInstance().getMaxTradeNum, params, true);
    }

    private void showPopupWindow(String price, String amount, String bsFlag, String ocFlag, String value) {
        if (null == mWindow)
            return;

       /* mWindow.setData(mUser.getAccount(), "Au(T+D)", price, amount,
                bsFlag, ocFlag, value, (view) -> {
                    limitOrder("Au(T+D)", price, amount, bsFlag, "0");

                    mWindow.dismiss();
                });
        mWindow.showAtLocation(mBinding.lltitle, Gravity.BOTTOM, 0, 0);*/
    }

    private void limitOrder(String contractId, String price, String amount, String bsFlag, String ocFlag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("entrustPrice", String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", amount);
        params.put("bsFlag", bsFlag);
        params.put("ocFlag", ocFlag);
        params.put("tradingType", "0");

        sendRequest(TradeService.getInstance().limitOrder, params, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
