package com.jme.lsgoldtrade.ui.transaction;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.fchart.FData;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentPlaceOrderBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.PlaceOrderPopupWindow;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class PlaceOrderFragment extends JMEBaseFragment implements FChart.OnPriceClickListener {

    private FragmentPlaceOrderBinding mBinding;

    private boolean bVisibleToUser = false;
    private boolean bFlag = true;
    private int mSelectItem = 0;
    private int mBsFlag = 0;
    private int mOcFlag = 0;
    private float mPriceMove = 0.00f;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private String mPlaceOrderPrice;
    private String mPagingKey = "";
    private String[] mContracIDs;

    private List<PositionVo> mPositionVoList = new ArrayList<>();

    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private PlaceOrderPopupWindow mPlaceOrderPopupWindow;
    private Subscription mRxbus;
    private ContractInfoVo mContractInfoVo;
    private FiveSpeedVo mFiveSpeedVo;
    private AlertDialog mDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_TRANSACTION_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

                    getFiveSpeedQuotes(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_place_order;
    }

    @Override
    protected void initView() {
        super.initView();

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);
        mPlaceOrderPopupWindow = new PlaceOrderPopupWindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

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
                if (!mBinding.tvContractId.getText().toString().trim().equals("Ag(T+D)")) {
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
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentPlaceOrderBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        if (null != mHandler && !bVisibleToUser)
            mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;
            mPagingKey = "";
            mPositionVoList.clear();

            setContractNameData();
            getFiveSpeedQuotes(true);
            getPosition();
        } else {
            if (null != mHandler)
                mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;
            mPagingKey = "";
            mPositionVoList.clear();

            setContractNameData();
            getFiveSpeedQuotes(true);
            getPosition();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);
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

            if (mBinding.tvContractId.getText().toString().trim().equals("Ag(T+D)"))
                mBinding.etPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
            else
                mBinding.etPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if (null != mContract) {
                mSelectItem = mContract.getContractIDPosition(mBinding.tvContractId.getText().toString());
                mContractInfoVo = mContract.getContractInfoFromID(mBinding.tvContractId.getText().toString());

                setContractData();
            }
        }
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

                if (mBinding.tvContractId.getText().toString().trim().equals("Ag(T+D)"))
                    mBinding.etPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
                else
                    mBinding.etPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                AppConfig.Select_ContractId = contractID;
                setContractData();

                mHandler.removeMessages(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA);

                bFlag = true;

                getFiveSpeedQuotes(true);
            }

            mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_down));

            mDialog.dismiss();
        });

        mDialog = builder.create();
        mDialog.show();
    }

    private void showPlaceOrderPopupWindow(String contractID, String price, String amount, int bsFlag) {
        if (null == mPlaceOrderPopupWindow || mPlaceOrderPopupWindow.isShowing())
            return;

        mPlaceOrderPopupWindow.setData(mUser.getAccount(), contractID, price, amount, String.valueOf(bsFlag), (view) -> {
            getStatus();

            mPlaceOrderPopupWindow.dismiss();
        });
        mPlaceOrderPopupWindow.showAtLocation(mBinding.etAmount, Gravity.BOTTOM, 0, 0);
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void doTransaction() {
        String contractID = mBinding.tvContractId.getText().toString();
        String amount = mBinding.etAmount.getText().toString();
        long holdAmount = Long.parseLong(amount) + getPosition(contractID);

        String priceStr = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(priceStr)) {
            if (mBsFlag == 1)
                mPlaceOrderPrice = mFiveSpeedVo.getFiveAskLists().get(4)[1];
            else
                mPlaceOrderPrice = mFiveSpeedVo.getFiveBidLists().get(0)[1];
        } else {
            mPlaceOrderPrice = priceStr;
        }

        if (TextUtils.isEmpty(contractID))
            showShortToast(R.string.transaction_contract_error);
        else if (TextUtils.isEmpty(mPlaceOrderPrice) || mPlaceOrderPrice.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            showShortToast(R.string.transaction_price_error);
        else if (new BigDecimal(mPlaceOrderPrice).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            showShortToast(R.string.transaction_limit_down_price_error);
        else if (new BigDecimal(mPlaceOrderPrice).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
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
            showPlaceOrderPopupWindow(contractID, mPlaceOrderPrice, amount, mBsFlag);
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price))
            price = mBinding.tvLastPrice.getText().toString();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    public long getPosition(String contractID) {
        if (null == mPositionVoList || 0 == mPositionVoList.size() || TextUtils.isEmpty(contractID))
            return 0;

        long value = 0;

        for (PositionVo positionVo : mPositionVoList) {
            if (null != positionVo) {
                if (positionVo.getContractId().equalsIgnoreCase(contractID)) {
                    value = value + positionVo.getPosition();
                }
            }
        }

        return value;
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getFiveSpeedQuotes(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", mBinding.tvContractId.getText().toString().trim());

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, enable, false, false);
    }

    private void getPosition() {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false, false, false);
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
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    List<FiveSpeedVo> list;

                    try {
                        list = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    mFiveSpeedVo = list.get(0);

                    if (null == mFiveSpeedVo || !mBinding.tvContractId.getText().toString().equals(mFiveSpeedVo.getContractId()))
                        return;

                    String lastSettlePrice = mFiveSpeedVo.getLastSettlePrice();
                    String latestPrice = mFiveSpeedVo.getLatestPriceValue();
                    mLowerLimitPrice = mFiveSpeedVo.getLowerLimitPrice();
                    mHighLimitPrice = mFiveSpeedVo.getHighLimitPrice();

                    if (TextUtils.isEmpty(lastSettlePrice))
                        return;

                    mBinding.tvLastPrice.setText(latestPrice);
                    mBinding.tvLastPrice.setTextColor(ContextCompat.getColor(mContext, new BigDecimal(latestPrice).compareTo(new BigDecimal(0)) == 0
                            ? R.color.color_text_black : MarketUtil.getMarketStateColor(new BigDecimal(latestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
                    mBinding.tvLimitDownPrice.setText(mLowerLimitPrice);
                    mBinding.tvLimitUpPrice.setText(mHighLimitPrice);
                    mBinding.tvLastAmount.setText(MarketUtil.getVolumeValue(String.valueOf(new BigDecimal(mFiveSpeedVo.getTurnover()).divide(new BigDecimal(100))), false));
                    mBinding.fchartSale.setData(mFiveSpeedVo.getFiveAskLists(), FData.TYPE_SELL, lastSettlePrice);
                    mBinding.fchartBuy.setData(mFiveSpeedVo.getFiveBidLists(), FData.TYPE_BUY, lastSettlePrice);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRANSACTION_UPDATE_DATA, getTimeInterval());
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

                    if (null == positionPageVo)
                        return;

                    boolean hasNext = positionPageVo.isHasNext();
                    mPagingKey = positionPageVo.getPagingKey();

                    mPositionVoList.addAll(positionPageVo.getPositionList());

                    if (hasNext)
                        getPosition();
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
                        limitOrder(mBinding.tvContractId.getText().toString(), mPlaceOrderPrice, mBinding.etAmount.getText().toString(), mBsFlag, mOcFlag);
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
            hiddenKeyBoard();

            if (null != mContracIDs && mContracIDs.length > 0) {
                mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_up));

                showContractNameDialog();
            }
        }

        public void onClickLimitDownPrice() {
            hiddenKeyBoard();

            mBinding.etPrice.setText(mLowerLimitPrice);
        }

        public void onClickLimitUpPrice() {
            hiddenKeyBoard();

            mBinding.etPrice.setText(mHighLimitPrice);
        }

        public void onClickLatestPrice() {
            hiddenKeyBoard();

            String price = mBinding.tvLastPrice.getText().toString();

            if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
                return;

            mBinding.etPrice.setText(price);
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

                doTransaction();
            }
        }

        public void onClickSaleEmpty() {
            hiddenKeyBoard();

            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                mBsFlag = 2;
                mOcFlag = 0;

                doTransaction();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
