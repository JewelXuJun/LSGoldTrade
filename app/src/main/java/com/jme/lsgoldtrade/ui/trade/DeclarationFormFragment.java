package com.jme.lsgoldtrade.ui.trade;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;

import com.datai.common.charts.fchart.FData;
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
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.generated.callback.OnClickListener;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class DeclarationFormFragment extends JMEBaseFragment {

    private FragmentDeclarationFormBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;
    private String[] mContracIDs;

    private TabViewPagerAdapter mAdapter;
    private ContractInfoVo mContractInfoVo;
    private TenSpeedVo mTenSpeedVo;
    private AlertDialog mDialog;
    private OrderPopUpWindow mWindow;
    private Subscription mRxbus;

    private boolean bVisibleToUser = false;
    private boolean bFlag = true;
    private boolean bEveningUp = false;
    private int mSelectItem = 0;
    private int mPriceType = TYPE_RIVALPRICE;
    private float mPriceMove = 0.00f;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private String mPositionType;

    private static int TYPE_NONE = 0;
    private static int TYPE_RIVALPRICE = 1;
    private static int TYPE_QUEUINGPRICE = 2;
    private static int TYPE_LASTPRICE = 3;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_TRADE_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

                    getTenSpeedQuotes();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_UPDATE_DATA, getTimeInterval());

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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        mWindow = new OrderPopUpWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);

        initInfoTabs();
        initContractNameValue();
        setPriceTypeLayout();
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
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Lentth_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Lentth_Limit + 1));

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
                if (!TextUtils.isEmpty(s.toString()))
                    mPriceType = TYPE_NONE;

                setPriceTypeLayout();
            }
        });
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
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

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
                mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);
        }

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser) {
            bFlag = true;

            setContractNameData();
            getTenSpeedQuotes();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initInfoTabs() {
        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.trade_hold_position);
        mTabTitles[1] = mContext.getResources().getString(R.string.trade_entrust);
        mTabTitles[2] = mContext.getResources().getString(R.string.trade_deal);
        mTabTitles[3] = mContext.getResources().getString(R.string.trade_cancel_order);

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
        mBinding.tablayout.post(() -> setIndicator(mBinding.tablayout, 30, 30));
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

                    if (null == object) {
                        bEveningUp = false;

                        mBinding.tvPriceEqual.setText(R.string.text_no_data_default);
                    } else {
                        PositionVo positionVo = (PositionVo) object;

                        if (null == positionVo) {
                            bEveningUp = false;

                            mBinding.tvPriceEqual.setText(R.string.text_no_data_default);
                        } else {
                            bEveningUp = true;
                            mPositionType = positionVo.getType();
                            mPriceType = TYPE_RIVALPRICE;

                            String contractID = positionVo.getContractId();

                            if (!contractID.equals(AppConfig.Select_ContractId)) {
                                mSelectItem = mContract.getContractIDPosition(contractID);

                                AppConfig.Select_ContractId = contractID;

                                mBinding.tvContractId.setText(contractID);

                                mTenSpeedVo = null;
                                mContractInfoVo = mContract.getContractInfoFromID(contractID);
                            }

                            mBinding.etPrice.setText("");
                            mBinding.etPrice.clearFocus();
                            mBinding.etAmount.setText(String.valueOf(positionVo.getPosition() - positionVo.getOffsetFrozen()));

                            setPriceTypeLayout();
                            setContractData();

                            mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

                            getTenSpeedQuotes();
                        }
                    }

                    break;
            }
        });
    }

    private void setContractNameData() {
        if (null == mBinding)
            return;

        if (!mBinding.tvContractId.getText().toString().equals(AppConfig.Select_ContractId)) {
            mBinding.tvContractId.setText(AppConfig.Select_ContractId);

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

                if (bEveningUp) {
                    bEveningUp = false;

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_HOLDPOSITION_UNSELECT, null);
                }

                String contractID = mContracIDs[mSelectItem];

                mBinding.tvContractId.setText(contractID);
                mBinding.etPrice.setText("");
                mBinding.etAmount.setText("");
                mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
                mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
                mContractInfoVo = mContract.getContractInfoFromID(contractID);
                mPriceType = TYPE_RIVALPRICE;

                setPriceTypeLayout();
                AppConfig.Select_ContractId = contractID;
                setContractData();

                mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

                getTenSpeedQuotes();
            }

            mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_down));

            mDialog.dismiss();
        });

        mDialog = builder.create();
        mDialog.show();
    }

    private void setPriceTypeLayout() {
        mBinding.btnRivalPrice.setBackground(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnRivalPrice.setTextColor(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnQueuingPrice.setBackground(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnQueuingPrice.setTextColor(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnLastPrice.setBackground(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnLastPrice.setTextColor(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));

        if (mPriceType == TYPE_RIVALPRICE)
            mBinding.etPrice.setHint(R.string.market_rival_price);
        else if (mPriceType == TYPE_QUEUINGPRICE)
            mBinding.etPrice.setHint(R.string.market_queuing_price);
        else if (mPriceType == TYPE_LASTPRICE)
            mBinding.etPrice.setHint(R.string.market_last_price);
        else
            mBinding.etPrice.setHint(R.string.market_price_hint);

        setPriceData();
    }

    private void setPriceData() {
        if (null == mTenSpeedVo) {
            mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
            mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
            mBinding.tvPriceEqual.setText(R.string.text_no_data_default);
        } else {
            List<String[]> askLists = mTenSpeedVo.getAskLists();
            List<String[]> bidLists = mTenSpeedVo.getBidLists();

            if (mPriceType == TYPE_NONE) {
                String price = MarketUtil.formatValue(mBinding.etPrice.getText().toString(), 2);

                mBinding.tvPriceBuyMore.setText(TextUtils.isEmpty(price) ? mContext.getResources().getString(R.string.text_no_data_default) : price);
                mBinding.tvPriceSaleEmpty.setText(TextUtils.isEmpty(price) ? mContext.getResources().getString(R.string.text_no_data_default) : price);
                mBinding.tvPriceEqual.setText(bEveningUp ? (TextUtils.isEmpty(price) ? mContext.getResources().getString(R.string.text_no_data_default) : price)
                        : mContext.getResources().getString(R.string.text_no_data_default));
            } else if (mPriceType == TYPE_RIVALPRICE) {
                mBinding.tvPriceBuyMore.setText(askLists.get(9)[1]);
                mBinding.tvPriceSaleEmpty.setText(bidLists.get(0)[1]);
                mBinding.tvPriceEqual.setText(bEveningUp ? (mPositionType.equals("多") ? bidLists.get(0)[1] : askLists.get(9)[1])
                        : mContext.getResources().getString(R.string.text_no_data_default));
            } else if (mPriceType == TYPE_QUEUINGPRICE) {
                mBinding.tvPriceBuyMore.setText(bidLists.get(0)[1]);
                mBinding.tvPriceSaleEmpty.setText(askLists.get(9)[1]);
                mBinding.tvPriceEqual.setText(bEveningUp ? (mPositionType.equals("多") ? askLists.get(9)[1] : bidLists.get(0)[1])
                        : mContext.getResources().getString(R.string.text_no_data_default));
            } else if (mPriceType == TYPE_LASTPRICE) {
                mBinding.tvPriceBuyMore.setText(mTenSpeedVo.getLatestPrice());
                mBinding.tvPriceSaleEmpty.setText(mTenSpeedVo.getLatestPrice());
                mBinding.tvPriceEqual.setText(bEveningUp ? mTenSpeedVo.getLatestPrice()
                        : mContext.getResources().getString(R.string.text_no_data_default));
            } else {
                mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
                mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
                mBinding.tvPriceEqual.setText(R.string.text_no_data_default);
            }
        }
    }

    private void setContractData() {
        if (null == mContractInfoVo) {
            mPriceMove = 0;
            mMinOrderQty = 0;
            mMaxOrderQty = 0;
        } else {
            mPriceMove = new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            mMinOrderQty = mContractInfoVo.getMinOrderQty();
            mMaxOrderQty = mContractInfoVo.getMaxOrderQty();

            if (!bEveningUp)
                mBinding.etAmount.setText(String.valueOf(mMinOrderQty));
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

    private void showPopupWindow(String contractID, String price, String amount, int bsFlag, int ocFlag) {
        if (null == mWindow)
            return;

        mWindow.setData(mUser.getAccount(), contractID, price, amount,
                bsFlag == 1 ? mContext.getResources().getString(R.string.trade_buy) : mContext.getResources().getString(R.string.trade_sale), (view) -> {
                    limitOrder(contractID, price, amount, bsFlag, ocFlag);

                    mWindow.dismiss();
                });
        mWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
    }

    private void doTrade(int bsFlag, int ocFlag) {
        String contractID = mBinding.tvContractId.getText().toString();
        String price = bsFlag == 1 ? mBinding.tvPriceBuyMore.getText().toString() : mBinding.tvPriceSaleEmpty.getText().toString();
        String amount = mBinding.etAmount.getText().toString();

        if (TextUtils.isEmpty(contractID))
            showShortToast(R.string.trade_contract_error);
        else if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            showShortToast(R.string.trade_price_error);
        else if (new BigDecimal(price).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            showShortToast(R.string.trade_limit_down_price_error);
        else if (new BigDecimal(price).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
            showShortToast(R.string.trade_limit_up_price_error);
        else if (TextUtils.isEmpty(amount))
            showShortToast(R.string.trade_number_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMinOrderQty)) == -1)
            showShortToast(R.string.trade_limit_min_amount_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
            showShortToast(R.string.trade_limit_max_amount_error);
        else
            showPopupWindow(contractID, price, amount, bsFlag, ocFlag);
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getTenSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", AppConfig.Select_ContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void limitOrder(String contractId, String price, String amount, int bsFlag, int ocFlag) {
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
                    mBinding.tvPrice.setTextColor(ContextCompat.getColor(mContext,
                            MarketUtil.getMarketStateColor(new BigDecimal(latestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
                    mBinding.tvLimitDownPrice.setText(mLowerLimitPrice);
                    mBinding.tvLimitUpPrice.setText(mHighLimitPrice);
                    mBinding.tvAmount.setText(MarketUtil.getVolumeValue(String.valueOf(mTenSpeedVo.getTurnover()), false));
                    mBinding.fchartSale.setData(mTenSpeedVo.getAskLists(), FData.TYPE_SELL, lastSettlePrice);
                    mBinding.fchartBuy.setData(mTenSpeedVo.getBidLists(), FData.TYPE_BUY, lastSettlePrice);

                    setPriceData();
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_UPDATE_DATA, getTimeInterval());
                }

                break;
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_success);

                    bEveningUp = false;
                    mBinding.tvPriceEqual.setText(R.string.text_no_data_default);

                    RxBus.getInstance().post(Constants.RxBusConst.RxBus_DeclarationForm_UPDATE, null);
                }

                break;
        }
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
            mPriceType = TYPE_NONE;
            mBinding.etPrice.setText(mLowerLimitPrice);

            hiddenKeyBoard();
            setPriceTypeLayout();
        }

        public void onClickLimitUpPrice() {
            mPriceType = TYPE_NONE;
            mBinding.etPrice.setText(mHighLimitPrice);

            hiddenKeyBoard();
            setPriceTypeLayout();
        }

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mLowerLimitPrice))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mLowerLimitPrice)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etPrice.setSelection(TextUtils.isEmpty(price) ? 0 : price.length());
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqual.setText(bEveningUp ? MarketUtil.formatValue(price, 2)
                        : mContext.getResources().getText(R.string.text_no_data_default));
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
                showShortToast(R.string.trade_limit_up_price_error);

                mBinding.etPrice.setSelection(price.length());
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqual.setText(bEveningUp ? MarketUtil.formatValue(price, 2)
                        : mContext.getResources().getText(R.string.text_no_data_default));
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickRivalPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_RIVALPRICE)
                return;

            mPriceType = TYPE_RIVALPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }

        public void onClickQueuingPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_QUEUINGPRICE)
                return;

            mPriceType = TYPE_QUEUINGPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }

        public void onClickLastPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_LASTPRICE)
                return;

            mPriceType = TYPE_LASTPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                showShortToast(R.string.trade_limit_min_amount_error);
            else
                mBinding.etAmount.setText(String.valueOf(value));
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
                showShortToast(R.string.trade_limit_max_amount_error);
            else
                mBinding.etAmount.setText(String.valueOf(value));
        }

        public void onClickBuyMore() {
            hiddenKeyBoard();

            doTrade(1, 0);
        }

        public void onClickSaleEmpty() {
            hiddenKeyBoard();

            doTrade(2, 0);
        }

        public void onClickEveningUp() {
            hiddenKeyBoard();

            if (bEveningUp)
                doTrade(mPositionType.equals("多") ? 2 : 1, 1);
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
