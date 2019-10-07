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
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.AnalystVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.ui.trade.TradeMessagePopUpWindow;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.EveningUpPopupWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 行情研判
 */
@Route(path = Constants.ARouterUriConst.MARKETJUDGMENT)
public class MarketJudgmentActivity extends JMEBaseActivity {

    private ActivityMarketJudgmentBinding mBinding;

    private List<AnalystVo> mAnalystVoList;
    private List<String> mTabTitles = new ArrayList<>();
    private List<MarketJudgmentFragment> mFragmentList = new ArrayList<>();

    private String mContractID;
    private int mBsFlag = 0;
    private int mOcFlag = 0;

    private ContractInfoVo mContractInfoVo;
    private TenSpeedVo mTenSpeedVo;
    private AccountVo mAccountVo;

    private MarketJudgmentPagerAdapter mAdapter;
    private EveningUpPopupWindow mEveningUpPopupWindow;
    private TradeMessagePopUpWindow mTradeMessagePopUpWindow;
    private MarketTradePopupWindow mMarketTradePopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_judgment;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.market_judgment, true);

        mEveningUpPopupWindow = new EveningUpPopupWindow(this);
        mEveningUpPopupWindow.setOutsideTouchable(true);
        mEveningUpPopupWindow.setFocusable(true);

        mTradeMessagePopUpWindow = new TradeMessagePopUpWindow(mContext);
        mTradeMessagePopUpWindow.setOutsideTouchable(true);
        mTradeMessagePopUpWindow.setFocusable(true);

        mMarketTradePopupWindow = new MarketTradePopupWindow(mContext);
        mMarketTradePopupWindow.setOutsideTouchable(true);
        mMarketTradePopupWindow.setFocusable(true);

        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);
        mConfirmPopupwindow.setOutsideTouchable(true);
        mConfirmPopupwindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        getAnalystList();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityMarketJudgmentBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_MARKETDETAIL_QUICK:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    List<String> list = (List<String>) object;

                    if (null == list || 5 != list.size())
                        return;

                    limitOrder(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));

                    break;
            }
        });
    }

    private void initTabLayout() {
        mAdapter = new MarketJudgmentPagerAdapter(getSupportFragmentManager());

        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(1);
        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    private void getAnalystList() {
        sendRequest(ManagementService.getInstance().analystList, new HashMap<>(), true);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    private void getAccount() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, true);
    }

    private void position() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("pagingKey", "");

        sendRequest(TradeService.getInstance().position, params, false);
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
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "AnalystList":
                if (head.isSuccess()) {
                    try {
                        mAnalystVoList = (List<AnalystVo>) response;
                    } catch (Exception e) {
                        mAnalystVoList = null;

                        e.printStackTrace();
                    }

                    if (null == mAnalystVoList || 0 == mAnalystVoList.size())
                        return;

                    for (int i = 0; i < mAnalystVoList.size(); i++) {
                        AnalystVo analystVo = mAnalystVoList.get(i);

                        if (null != analystVo) {
                            mTabTitles.add(analystVo.getName());
                            mFragmentList.add(MarketJudgmentFragment.newInstance(mAnalystVoList.get(i).getId(), "Au(T+D)"));
                        }
                    }

                    initTabLayout();
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
                            mTradeMessagePopUpWindow.showAtLocation(mBinding.tablayout, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        getAccount();
                    }
                }

                break;
            case "Account":
                if (head.isSuccess()) {
                    try {
                        mAccountVo = (AccountVo) response;
                    } catch (Exception e) {
                        mAccountVo = null;

                        e.printStackTrace();
                    }

                    position();
                }

                break;
            case "Position":
                if (head.isSuccess()) {
                    PositionPageVo positionPageVo;

                    try {
                        positionPageVo = (PositionPageVo) response;
                    } catch (Exception e) {
                        positionPageVo = null;

                        e.printStackTrace();
                    }

                    PositionVo positionVoValue = null;

                    if (null != positionPageVo) {
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo && positionVo.getContractId().equals(mContractID)) {
                                    if (mBsFlag == 1 && positionVo.getType().equals("多"))
                                        positionVoValue = positionVo;
                                    else if (mBsFlag == 2 && positionVo.getType().equals("空"))
                                        positionVoValue = positionVo;
                                }
                            }
                        }
                    }

                    if (null != mMarketTradePopupWindow && !mMarketTradePopupWindow.isShowing()) {
                        mMarketTradePopupWindow.setData(mTenSpeedVo, mAccountVo, positionVoValue, mContractInfoVo,
                                mUser.getAccount(), mBsFlag, mOcFlag);
                        mMarketTradePopupWindow.showAtLocation(mBinding.tablayout, Gravity.BOTTOM, 0, 0);
                    }
                }

                break;
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_success);
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.trade_money_error),
                                    mContext.getResources().getString(R.string.trade_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.tablayout, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickBuyMore() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                if (null == mFragmentList || 0 == mFragmentList.size())
                    return;

                MarketJudgmentFragment marketJudgmentFragment = mFragmentList.get(mBinding.tabViewpager.getCurrentItem());

                if (null == marketJudgmentFragment)
                    return;

                mContractID = marketJudgmentFragment.getContractID();
                mContractInfoVo = marketJudgmentFragment.getContractInfo();
                mTenSpeedVo = marketJudgmentFragment.getTenSpeedVo();

                if (TextUtils.isEmpty(mContractID) || null == mTenSpeedVo || null == mContractInfoVo)
                    return;

                mBsFlag = 1;
                mOcFlag = 0;

                getStatus();
            }
        }

        public void onClickSaleEmpty() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                if (null == mFragmentList || 0 == mFragmentList.size())
                    return;

                MarketJudgmentFragment marketJudgmentFragment = mFragmentList.get(mBinding.tabViewpager.getCurrentItem());

                if (null == marketJudgmentFragment)
                    return;

                mContractID = marketJudgmentFragment.getContractID();
                mContractInfoVo = marketJudgmentFragment.getContractInfo();
                mTenSpeedVo = marketJudgmentFragment.getTenSpeedVo();

                if (TextUtils.isEmpty(mContractID) || null == mTenSpeedVo || null == mContractInfoVo)
                    return;

                mBsFlag = 2;
                mOcFlag = 0;

                getStatus();
            }
        }

        public void onClickDeclarationForm() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                AppConfig.Select_ContractId = "Au(T+D)";

                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, "Au(T+D)");
                ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                finish();
            }
        }
    }

    final class MarketJudgmentPagerAdapter extends FragmentPagerAdapter {

        public MarketJudgmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (null == mFragmentList || 0 == mFragmentList.size())
                return null;

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
