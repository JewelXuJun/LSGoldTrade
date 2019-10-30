package com.jme.lsgoldtrade.ui.trade;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.fchart.FData;
import com.google.android.material.tabs.TabLayout;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentDeclarationFormBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.ui.transaction.CancelOrderPopUpWindow;
import com.jme.lsgoldtrade.view.PlaceOrderPopupWindow;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.ui.transaction.EveningUpPopupWindow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 报单
 */
public class DeclarationFormFragment extends JMEBaseFragment implements FChart.OnPriceClickListener {

    private FragmentDeclarationFormBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;
    private String[] mContracIDs;

    private TabViewPagerAdapter mAdapter;
    private ContractInfoVo mContractInfoVo;
    private ContractInfoVo mEveningUpContractInfoVo;
    private TenSpeedVo mTenSpeedVo;
    private PositionVo mPositionVo;
    private AlertDialog mDialog;
    private PlaceOrderPopupWindow mPlaceOrderPopupWindow;
    private CancelOrderPopUpWindow mCancelWindow;
    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;
    private EveningUpPopupWindow mEveningUpPopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private Subscription mRxbus;

    private boolean bVisibleToUser = false;
    private boolean bFlag = true;
    private int mSelectItem = 0;
    private float mPriceMove = 0.00f;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private String mDeclarationFormPrice;
    private String mEveningUpContractID;
    private int mBsFlag = 0;
    private int mOcFlag = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_TRANSACTION_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

                    getTenSpeedQuotes();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_declaration_form;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentDeclarationFormBinding) mBindingUtil;

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);
        mEveningUpPopupWindow = new EveningUpPopupWindow(mContext);
        mPlaceOrderPopupWindow = new PlaceOrderPopupWindow(mContext);
        mCancelWindow = new CancelOrderPopUpWindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        initInfoTabs();
        initContractNameValue();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etPrice.setText(s);
                        mBinding.etPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etPrice.setText(s);
                    mBinding.etPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etPrice.setText(s.subSequence(0, 1));
                        mBinding.etPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.fchartBuy.setOnPriceClickListener(this);
        mBinding.fchartSale.setOnPriceClickListener(this);

        mCancelWindow.setOnDismissListener(() -> RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_CANCEL, null));
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        if (null != mHandler && !bVisibleToUser)
            mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

        super.onHiddenChanged(hidden);

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            setContractNameData();
            getTenSpeedQuotes();
        } else {
            if (null != mHandler)
                mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);
        }

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            setContractNameData();
            getTenSpeedQuotes();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initInfoTabs() {
        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.transaction_hold_positions);
        mTabTitles[1] = mContext.getResources().getString(R.string.trade_entrust);
        mTabTitles[2] = mContext.getResources().getString(R.string.trade_deal);
        mTabTitles[3] = mContext.getResources().getString(R.string.transaction_cancel_order);

        mFragmentArrays = new Fragment[4];
        mFragmentArrays[0] = new ItemHoldPositionFragment();
        mFragmentArrays[1] = new ItemEntrustFragment();
        mFragmentArrays[2] = new ItemDealFragment();
        mFragmentArrays[3] = new ItemCancelOrderFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(4);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    private void initContractNameValue() {
        if (null != mContract) {
            String listStr = mContract.getContractIDListStr();

            if (!TextUtils.isEmpty(listStr))
                mContracIDs = listStr.split(",");
        }
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_DECLARATIONFORM_HOLDPOSITION_SELECT:
                    Object object = message.getObject2();

                    mPositionVo = (PositionVo) object;

                    if (null == mPositionVo)
                        return;

                    mEveningUpContractID = mPositionVo.getContractId();
                    mEveningUpContractInfoVo = mContract.getContractInfoFromID(mEveningUpContractID);

                    getFiveSpeedQuotes(mEveningUpContractID);

                    break;
                case Constants.RxBusConst.RXBUS_DECLARATIONFORM_SHOW:
                    Object cancelObject = message.getObject2();

                    if (null == cancelObject)
                        return;

                    OrderPageVo.OrderBean orderBean = (OrderPageVo.OrderBean) cancelObject;

                    if (null == orderBean)
                        return;

                    if (null != mCancelWindow) {
                        String time = orderBean.getDeclareTime();
                        String contractId = orderBean.getContractId();

                        mCancelWindow.setData(contractId, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"),
                                MarketUtil.getTradeDirection(orderBean.getBsFlag()) + MarketUtil.getOCState(orderBean.getOcFlag()),
                                orderBean.getMatchPriceStr(), String.valueOf(orderBean.getEntrustNumber()),
                                String.valueOf(orderBean.getRemnantNumber()), MarketUtil.getEntrustState(orderBean.getStatus()),
                                (View) -> {
                                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_CANCEL, null);

                                    mCancelWindow.dismiss();
                                },
                                (View) -> {
                                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_CONFIRM, null);

                                    mCancelWindow.dismiss();
                                });
                        mCancelWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
                    }

                    break;
                case Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS:
                    mBinding.etPrice.setText("");
                    mBinding.etAmount.setText("1");

                    break;
            }
        });
    }

    private void setContractNameData() {
        if (null == mBinding)
            return;

        if (!mBinding.tvContractId.getText().toString().equals(AppConfig.Select_ContractId)) {
            mBinding.tvContractId.setText(AppConfig.Select_ContractId);
            mBinding.etPrice.setText("");

            if (null != mContract) {
                mSelectItem = mContract.getContractIDPosition(mBinding.tvContractId.getText().toString());
                mContractInfoVo = mContract.getContractInfoFromID(mBinding.tvContractId.getText().toString());

                setContractData();
            }
        }
    }

    private void showContractNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(mContracIDs, mSelectItem, (dialogInterface, position) -> {
            if (mSelectItem != position) {
                mSelectItem = position;

                String contractID = mContracIDs[mSelectItem];

                mBinding.tvContractId.setText(contractID);
                mBinding.etPrice.setText("");
                mBinding.etAmount.setText("1");
                mContractInfoVo = mContract.getContractInfoFromID(contractID);

                AppConfig.Select_ContractId = contractID;
                setContractData();

                mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

                getTenSpeedQuotes();
            }

            mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_down));

            mDialog.dismiss();
        });

        mDialog = builder.create();
        mDialog.show();
    }

    private void setContractData() {
        if (null == mContractInfoVo) {
            mPriceMove = 0;
            mMinOrderQty = 0;
            mMaxOrderQty = 0;
            mMaxHoldQty = 0;
        } else {
            mPriceMove = new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            mMinOrderQty = mContractInfoVo.getMinOrderQty();
            mMaxOrderQty = mContractInfoVo.getMaxOrderQty();
            mMaxHoldQty = mContractInfoVo.getMaxHoldQty();

            mBinding.etAmount.setText(mMinOrderQty == -1 ? "1" : String.valueOf(mMinOrderQty));
        }
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price))
            price = mBinding.tvPrice.getText().toString();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void showPopupWindow(String contractID, String price, String amount, int bsFlag) {
        if (null == mPlaceOrderPopupWindow || mPlaceOrderPopupWindow.isShowing())
            return;

        mPlaceOrderPopupWindow.setData(mUser.getAccount(), contractID, price, amount, String.valueOf(bsFlag), (view) -> {
            getStatus();

            mPlaceOrderPopupWindow.dismiss();
        });
        mPlaceOrderPopupWindow.showAtLocation(mBinding.etAmount, Gravity.BOTTOM, 0, 0);
    }

    private void doTrade() {
        String contractID = mBinding.tvContractId.getText().toString();
        String amount = mBinding.etAmount.getText().toString();
        long holdAmount = Long.parseLong(amount) + ((ItemHoldPositionFragment) mFragmentArrays[0]).getPosition(contractID);

        String priceStr = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(priceStr)) {
            if (mBsFlag == 1)
                mDeclarationFormPrice = mTenSpeedVo.getFiveAskLists().get(4)[1];
            else
                mDeclarationFormPrice = mTenSpeedVo.getFiveBidLists().get(0)[1];
        } else {
            mDeclarationFormPrice = priceStr;
        }

        if (TextUtils.isEmpty(contractID))
            showShortToast(R.string.transaction_contract_error);
        else if (TextUtils.isEmpty(mDeclarationFormPrice) || mDeclarationFormPrice.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            showShortToast(R.string.transaction_price_error);
        else if (new BigDecimal(mDeclarationFormPrice).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            showShortToast(R.string.transaction_limit_down_price_error);
        else if (new BigDecimal(mDeclarationFormPrice).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
            showShortToast(R.string.transaction_limit_up_price_error);
        else if (TextUtils.isEmpty(amount))
            showShortToast(R.string.transaction_number_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
            showShortToast(R.string.transaction_number_error_zero);
        else if (mMinOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMinOrderQty)) == -1)
            showShortToast(R.string.transaction_limit_min_amount_error);
        else if (mMaxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
            showShortToast(R.string.transaction_limit_max_amount_error);
        else if (mMaxHoldQty != -1 && new BigDecimal(holdAmount).compareTo(new BigDecimal(mMaxHoldQty)) == 1)
            showShortToast(R.string.transaction_limit_max_amount_error2);
        else
            showPopupWindow(contractID, mDeclarationFormPrice, amount, mBsFlag);
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getFiveSpeedQuotes(String contractID) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", contractID);

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void getTenSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", AppConfig.Select_ContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    private void limitOrder(String contractId, String price, String amount, int bsFlag, int ocFlag) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("entrustPrice", String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", amount);
        params.put("bsFlag", String.valueOf(bsFlag));
        params.put("ocFlag", String.valueOf(ocFlag));
        params.put("tradingType", "0");

        sendRequest(TradeService.getInstance().limitOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
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

                    mTenSpeedVo = list.get(0);

                    if (null == mTenSpeedVo)
                        return;

                    if (!mBinding.tvContractId.getText().toString().equals(mTenSpeedVo.getContractId()))
                        return;

                    String lastSettlePrice = mTenSpeedVo.getLastSettlePrice();
                    String latestPrice = mTenSpeedVo.getLatestPrice();
                    mLowerLimitPrice = mTenSpeedVo.getLowerLimitPrice();
                    mHighLimitPrice = mTenSpeedVo.getHighLimitPrice();

                    if (TextUtils.isEmpty(lastSettlePrice))
                        return;

                    mBinding.tvPrice.setText(latestPrice);
                    mBinding.tvPrice.setTextColor(ContextCompat.getColor(mContext, new BigDecimal(latestPrice).compareTo(new BigDecimal(0)) == 0
                            ? R.color.color_text_black : MarketUtil.getMarketStateColor(new BigDecimal(latestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
                    mBinding.tvLimitDownPrice.setText(mLowerLimitPrice);
                    mBinding.tvLimitUpPrice.setText(mHighLimitPrice);
                    mBinding.tvAmount.setText(MarketUtil.getVolumeValue(String.valueOf(new BigDecimal(mTenSpeedVo.getTurnover()).divide(new BigDecimal(100))), false));
                    mBinding.fchartSale.setData(mTenSpeedVo.getFiveAskLists(), FData.TYPE_SELL, lastSettlePrice);
                    mBinding.fchartBuy.setData(mTenSpeedVo.getFiveBidLists(), FData.TYPE_BUY, lastSettlePrice);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA, getTimeInterval());
                }

                break;
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    List<FiveSpeedVo> fiveSpeedVoList;

                    try {
                        fiveSpeedVoList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        fiveSpeedVoList = null;

                        e.getMessage();
                    }

                    if (null == fiveSpeedVoList || 0 == fiveSpeedVoList.size())
                        return;

                    FiveSpeedVo fiveSpeedVo = fiveSpeedVoList.get(0);

                    if (null == fiveSpeedVo)
                        return;

                    if (!mEveningUpContractID.equals(fiveSpeedVo.getContractId()))
                        return;

                    if (null != mEveningUpPopupWindow && !mEveningUpPopupWindow.isShowing() && null != mEveningUpContractInfoVo) {
                        String lowerLimitPrice = fiveSpeedVo.getLowerLimitPrice();
                        String highLimitPrice = fiveSpeedVo.getHighLimitPrice();
                        String type = mPositionVo.getType();
                        long minOrderQty = mEveningUpContractInfoVo.getMinOrderQty();
                        long maxOrderQty = mEveningUpContractInfoVo.getMaxOrderQty();
                        long maxHoldQty = mEveningUpContractInfoVo.getMaxHoldQty();
                        long maxAmount = mPositionVo.getPosition();

                        mEveningUpPopupWindow.setData(mUser.getAccount(), mEveningUpContractID,
                                type.equals("多") ? fiveSpeedVo.getFiveBidLists().get(0)[1] : fiveSpeedVo.getFiveAskLists().get(4)[1],
                                type, new BigDecimal(mEveningUpContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue(),
                                lowerLimitPrice, highLimitPrice, minOrderQty, maxOrderQty, maxHoldQty, maxAmount,
                                (view) -> {
                                    String price = mEveningUpPopupWindow.getPrice();
                                    String amount = mEveningUpPopupWindow.getAmount();

                                    if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default))) {
                                        showShortToast(R.string.transaction_price_error);
                                    } else if (new BigDecimal(price).compareTo(new BigDecimal(lowerLimitPrice)) == -1) {
                                        showShortToast(R.string.transaction_limit_down_price_error);
                                    } else if (new BigDecimal(price).compareTo(new BigDecimal(highLimitPrice)) == 1) {
                                        showShortToast(R.string.transaction_limit_up_price_error);
                                    } else if (TextUtils.isEmpty(amount)) {
                                        showShortToast(R.string.transaction_number_error);
                                    } else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0) {
                                        showShortToast(R.string.transaction_number_error_zero);
                                    } else if (minOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(minOrderQty)) == -1) {
                                        showShortToast(R.string.transaction_limit_min_amount_error);
                                    } else if (mMaxOrderQty == -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMaxHoldQty == -1 ? maxAmount : Math.min(maxAmount, mMaxHoldQty))) == 1) {
                                        Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                                    } else if (mMaxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(Math.min(maxAmount, mMaxOrderQty))) == 1) {
                                        Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                                    } else {
                                        limitOrder(mEveningUpContractID, mEveningUpPopupWindow.getPrice(),
                                                mEveningUpPopupWindow.getAmount(), mPositionVo.getType().equals("多") ? 2 : 1, 1);

                                        mEveningUpPopupWindow.dismiss();
                                    }
                                });
                        mEveningUpPopupWindow.showAtLocation(mBinding.etAmount, Gravity.BOTTOM, 0, 0);
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
                        if (null != mTransactionMessagePopUpWindow && !mTransactionMessagePopUpWindow.isShowing()) {
                            mTransactionMessagePopUpWindow.setData(mContext.getResources().getString(R.string.transaction_account_error),
                                    mContext.getResources().getString(R.string.transaction_account_goto_recharge),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.RECHARGE).navigation();

                                        mTransactionMessagePopUpWindow.dismiss();
                                    });
                            mTransactionMessagePopUpWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        limitOrder(mBinding.tvContractId.getText().toString(), mDeclarationFormPrice, mBinding.etAmount.getText().toString(), mBsFlag, mOcFlag);
                    }
                }

                break;
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_success);

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_UPDATE, null);
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_money_error),
                                    mContext.getResources().getString(R.string.transaction_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
        }
    }

    @Override
    public void OnPriceClick(String price, String title) {
        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return;

        mBinding.etPrice.setText(price);

        hiddenKeyBoard();
    }

    public class ClickHandlers {

        public void onClickSelectContract() {
            if (null != mContracIDs && mContracIDs.length > 0) {
                mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_up));

                showContractNameDialog();
            }

            hiddenKeyBoard();
        }

        public void onClickLimitDownPrice() {
            mBinding.etPrice.setText(mLowerLimitPrice);

            hiddenKeyBoard();
        }

        public void onClickLimitUpPrice() {
            mBinding.etPrice.setText(mHighLimitPrice);

            hiddenKeyBoard();
        }

        public void onClickLatestPrice() {
            String price = mBinding.tvPrice.getText().toString();

            if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
                return;

            mBinding.etPrice.setText(price);

            hiddenKeyBoard();
        }

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mLowerLimitPrice))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mLowerLimitPrice)) == -1) {
                showShortToast(R.string.transaction_limit_down_price_error);

                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mHighLimitPrice))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mHighLimitPrice)) == 1) {
                showShortToast(R.string.transaction_limit_up_price_error);

                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (mMinOrderQty == -1) {
                if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 1)
                    mBinding.etAmount.setText(String.valueOf(value));
                else
                    showShortToast(R.string.transaction_number_error_zero);
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                    showShortToast(R.string.transaction_limit_min_amount_error);
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (mMaxOrderQty == -1) {
                if (mMaxHoldQty == -1) {
                    mBinding.etAmount.setText(String.valueOf(value));
                } else {
                    if (new BigDecimal(value).compareTo(new BigDecimal(mMaxHoldQty)) == 1)
                        showShortToast(R.string.transaction_limit_max_amount_error2);
                    else
                        mBinding.etAmount.setText(String.valueOf(value));
                }
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
                    showShortToast(R.string.transaction_limit_max_amount_error);
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickBuyMore() {
            hiddenKeyBoard();

            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                mBsFlag = 1;
                mOcFlag = 0;

                doTrade();
            }
        }

        public void onClickSaleEmpty() {
            hiddenKeyBoard();

            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                mBsFlag = 2;
                mOcFlag = 0;

                doTrade();
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

}
