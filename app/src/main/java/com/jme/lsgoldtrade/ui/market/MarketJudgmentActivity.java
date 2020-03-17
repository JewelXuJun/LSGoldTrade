package com.jme.lsgoldtrade.ui.market;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
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
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;
import com.jme.lsgoldtrade.view.SignedPopUpWindow;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

@Route(path = Constants.ARouterUriConst.MARKETJUDGMENT)
public class MarketJudgmentActivity extends JMEBaseActivity {

    private ActivityMarketJudgmentBinding mBinding;

    private List<AnalystVo> mAnalystVoList;
    private List<String> mTabTitles = new ArrayList<>();
    private List<MarketJudgmentFragment> mFragmentList = new ArrayList<>();

    private String mContractID;
    private String mPagingKey = "";
    private int mBsFlag = 0;
    private int mOcFlag = 0;
    private long mLongPositionMargin = 0;
    private long mShortPositionMargin = 0;

    private ContractInfoVo mContractInfoVo;
    private TenSpeedVo mTenSpeedVo;
    private AccountVo mAccountVo;
    private PositionVo mPositionVo;

    private MarketJudgmentPagerAdapter mAdapter;
    private MarketTradePopupWindow mMarketTradePopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private SignedPopUpWindow mSignedPopUpWindow;

    private Subscription mRxbus;
    private int mCallEntry = 0;
    private ConfirmSimplePopupwindow mTradingPasswordConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_judgment;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.market_judgment, true);

        mMarketTradePopupWindow = new MarketTradePopupWindow(this);
        mConfirmPopupwindow = new ConfirmPopupwindow(this);
        mSignedPopUpWindow = new SignedPopUpWindow(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mTradingPasswordConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mTradingPasswordConfirmSimplePopupwindow.setOutsideTouchable(false);
        mTradingPasswordConfirmSimplePopupwindow.setFocusable(false);
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
                case Constants.RxBusConst.RXBUS_HQYP_BUY_MORE:
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
                    mPagingKey = "";

                    queryLoginResult();
                    break;
                case Constants.RxBusConst.RXBUS_HQYP_SALE_EMPTY:
                    if (null == mFragmentList || 0 == mFragmentList.size())
                        return;

                    MarketJudgmentFragment marketJudgmentFragment2 = mFragmentList.get(mBinding.tabViewpager.getCurrentItem());

                    if (null == marketJudgmentFragment2)
                        return;

                    mContractID = marketJudgmentFragment2.getContractID();
                    mContractInfoVo = marketJudgmentFragment2.getContractInfo();
                    mTenSpeedVo = marketJudgmentFragment2.getTenSpeedVo();

                    if (TextUtils.isEmpty(mContractID) || null == mTenSpeedVo || null == mContractInfoVo)
                        return;

                    mBsFlag = 2;
                    mOcFlag = 0;
                    mPagingKey = "";

                    queryLoginResult();
                    break;
                case Constants.RxBusConst.RXBUS_HQYP_DECLARATION_FORM:
                    getUserPasswordSettingInfo();
                    AppConfig.Select_ContractId = "Au(T+D)";

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, "Au(T+D)");
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    finish();
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

    private void queryLoginResult() {
        DTRequest request = new DTRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), true, true);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        LoginResponse dtResponse = (LoginResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
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
        params.put("pagingKey", mPagingKey);

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

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true, false, false);
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
            case "QueryLoginResult":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }
//
//                    String isSign = userInfoVo.getIsSign();
//
//                    if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
//                        mUser.getCurrentUser().setIsSign("N");
//
//                        if (null != mSignedPopUpWindow && !mSignedPopUpWindow.isShowing())
//                            mSignedPopUpWindow.showAtLocation(mBinding.tablayout, Gravity.CENTER, 0, 0);
//                    } else {
                    getAccount();
//                    }
                } else {
//                    if (head.getCode().equals("-2012"))
//                        mUser.getCurrentUser().setIsSign("N");
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

                    if (null != positionPageVo) {
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo && positionVo.getContractId().equals(mContractID)) {
                                    if (positionVo.getType().equals("多")) {
                                        if (mBsFlag == 1)
                                            mPositionVo = positionVo;

                                        mLongPositionMargin = positionVo.getPositionMargin();
                                    } else if (positionVo.getType().equals("空")) {
                                        if (mBsFlag == 2)
                                            mPositionVo = positionVo;

                                        mShortPositionMargin = positionVo.getPositionMargin();
                                    }
                                }
                            }
                        }

                        boolean hasNext = positionPageVo.isHasNext();
                        mPagingKey = positionPageVo.getPagingKey();

                        if (hasNext) {
                            position();
                        } else {
                            if (null != mMarketTradePopupWindow && !mMarketTradePopupWindow.isShowing()) {
                                mMarketTradePopupWindow.setData(mTenSpeedVo, mAccountVo, mPositionVo, mContractInfoVo,
                                        Math.abs(mLongPositionMargin - mShortPositionMargin), mBsFlag, mOcFlag);
                                mMarketTradePopupWindow.showAtLocation(mBinding.tablayout, Gravity.BOTTOM, 0, 0);
                            }
                        }
                    }
                }

                break;
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_success);
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_money_error),
                                    mContext.getResources().getString(R.string.transaction_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.tablayout, Gravity.CENTER, 0, 0);
                        }
                    }
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
                        if (null != mTradingPasswordConfirmSimplePopupwindow && !mTradingPasswordConfirmSimplePopupwindow.isShowing()) {
                            mTradingPasswordConfirmSimplePopupwindow.setData(mContext.getResources().getString(R.string.security_setting_tips),
                                    mContext.getResources().getString(R.string.personal_setting),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDSETTING).navigation();

                                        mTradingPasswordConfirmSimplePopupwindow.dismiss();
                                    });
                            mTradingPasswordConfirmSimplePopupwindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
                        }
                    } else {

                        if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N")) {
                            if (mCallEntry == 10) {
                                //行情研判 买多
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_HQYP_BUY_MORE, null);
                            } else if (mCallEntry == 11) {
                                //行情研判 卖空
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_HQYP_SALE_EMPTY, null);
                            } else if (mCallEntry == 12) {
                                //行情研判 报单
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_HQYP_DECLARATION_FORM, null);
                            }

                            return;
                        }


                        int type = 1;
                        if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                            boolean isCanUseFingerPrint = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (FingerprintManagerCompat.from(mContext).isHardwareDetected()
                                        && FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints())
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
                                .withInt("callEntry", mCallEntry)
                                .navigation();
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
                //买多
                mCallEntry = 10;
                getUserPasswordSettingInfo();

            }
        }

        public void onClickSaleEmpty() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                //卖空
                mCallEntry = 11;
                getUserPasswordSettingInfo();

            }
        }

        public void onClickDeclarationForm() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                //报单
                mCallEntry = 12;
                getUserPasswordSettingInfo();
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
