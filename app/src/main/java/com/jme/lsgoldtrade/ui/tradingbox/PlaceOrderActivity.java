package com.jme.lsgoldtrade.ui.tradingbox;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityPlaceOrderBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.OrderVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.TradingBoxInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.ui.trade.TradeMessagePopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.MessagePopupwindow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 预埋单
 */
@Route(path = Constants.ARouterUriConst.PLACEORDER)
public class PlaceOrderActivity extends JMEBaseActivity {

    private ActivityPlaceOrderBinding mBinding;

    private TradingBoxPopupwindow mWindow;
    private TradeMessagePopUpWindow mTradeMessagePopUpWindow;
    private MessagePopupwindow mMessagePopupwindow;

    private String mType;
    private String mDirection;
    private String mTradeId;
    private String mDirectionSelected = "";
    private String mVariety;
    private String mID;
    private String mOpenTimeStart;
    private String mOpenTimeEnd;
    private String mEqualTimeStart;
    private String mEqualTimeEnd;
    private String mEarningsLine;
    private String mLossLine;
    private long mTransactionBalance;
    private long mHighLimitPrice;
    private boolean bEnoughFlag = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_place_order;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_prepaid_bill, true);

        mWindow = new TradingBoxPopupwindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);

        mTradeMessagePopUpWindow = new TradeMessagePopUpWindow(mContext);
        mTradeMessagePopUpWindow.setOutsideTouchable(true);
        mTradeMessagePopUpWindow.setFocusable(true);

        mMessagePopupwindow = new MessagePopupwindow(mContext);
        mMessagePopupwindow.setOutsideTouchable(true);
        mMessagePopupwindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getStringExtra("Type");
        mDirection = getIntent().getStringExtra("Direction");
        mTradeId = getIntent().getStringExtra("TradeId");

        if (mType.equals("Vote")) {
            mBinding.layoutDirection.setVisibility(View.GONE);
            mBinding.tvDirection.setVisibility(View.VISIBLE);
            mBinding.tvDirection.setText(mDirection.equals("0") ? R.string.text_more : R.string.text_empty);
            mBinding.tvDirection.setTextColor(mDirection.equals("0") ? ContextCompat.getColor(this, R.color.color_red)
                    : ContextCompat.getColor(this, R.color.color_green));
            mDirectionSelected = mDirection;
        } else {
            mBinding.layoutDirection.setVisibility(View.VISIBLE);
            mBinding.tvDirection.setVisibility(View.GONE);
            mDirectionSelected = mDirection;

            setEntrustDirectionLayout();
        }

        if (TextUtils.isEmpty(mTradeId))
            return;

        getBoxInfo();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
                calculateMoneyEnough(value.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityPlaceOrderBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAccount();
    }

    private void setEntrustDirectionLayout() {
        mBinding.tvMore.setBackground(mDirectionSelected.equals("0") ? ContextCompat.getDrawable(this, R.drawable.bg_btn_more_fill)
                : ContextCompat.getDrawable(this, R.drawable.bg_btn_more_solid));
        mBinding.tvMore.setTextColor(mDirectionSelected.equals("0") ? ContextCompat.getColor(this, R.color.white)
                : ContextCompat.getColor(this, R.color.color_red));
        mBinding.tvEmpty.setBackground(mDirectionSelected.equals("0") ? ContextCompat.getDrawable(this, R.drawable.bg_btn_empty_solid)
                : ContextCompat.getDrawable(this, R.drawable.bg_btn_empty_fill));
        mBinding.tvEmpty.setTextColor(mDirectionSelected.equals("0") ? ContextCompat.getColor(this, R.color.color_green)
                : ContextCompat.getColor(this, R.color.white));
        mBinding.imgMore.setVisibility(mDirectionSelected.equals("0") ? View.VISIBLE : View.GONE);
        mBinding.imgEmpty.setVisibility(mDirectionSelected.equals("0") ? View.GONE : View.VISIBLE);
    }

    private void calculateMoneyEnough(String number) {
        if (TextUtils.isEmpty(mVariety) || TextUtils.isEmpty(number)) {
            mBinding.tvBalanceMessage.setText(String.format(getString(R.string.trading_box_balance),
                    MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mTransactionBalance))));
        } else {
            int tradeUnit = 0;
            double marginRatio = 0;

            if (mVariety.equals("Au(T+D)")) {
                tradeUnit = 1000;
                marginRatio = 0.09;
            } else if (mVariety.equals("mAu(T+D)")) {
                tradeUnit = 100;
                marginRatio = 0.09;
            } else if (mVariety.equals("Ag(T+D)")) {
                tradeUnit = 1;
                marginRatio = 0.1;
            }

            BigDecimal amountValue = new BigDecimal(mHighLimitPrice).multiply(new BigDecimal(tradeUnit))
                    .multiply(new BigDecimal(marginRatio)).multiply(new BigDecimal(number));

            if (null == amountValue) {
                mBinding.tvBalanceMessage.setText(String.format(getString(R.string.trading_box_balance),
                        MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mTransactionBalance))));
            } else {
                if (amountValue.compareTo(new BigDecimal(0)) == 0 || amountValue.compareTo(new BigDecimal(0)) == -1) {
                    mBinding.tvBalanceMessage.setText(String.format(getString(R.string.trading_box_balance),
                            MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mTransactionBalance))));
                } else {
                    if (amountValue.compareTo(new BigDecimal(mTransactionBalance)) == 1) {
                        setBalanceNotEnoughMessage();
                    } else {
                        bEnoughFlag = true;

                        mBinding.tvBalanceMessage.setText(String.format(getString(R.string.trading_box_balance),
                                MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mTransactionBalance))));
                    }
                }
            }
        }
    }

    private void setBalanceNotEnoughMessage() {
        bEnoughFlag = false;

        String value = getString(R.string.trading_box_balance_not_enough);

        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_blue_deep)),
                value.length() - 2, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), value.length() - 2, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBinding.tvBalanceMessage.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvBalanceMessage.setText(spannableString);
    }

    private void getAccount() {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().account, params, false, false, false);
    }

    private void getBoxInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mTradeId);

        sendRequest(ManagementService.getInstance().getBoxInfo, params, true);
    }

    private void getTenSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", mVariety);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getUserAddedServicesStatus() {
        sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), true);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), false);
    }

    private void checkOrder() {
        sendRequest(ManagementService.getInstance().checkOrder, new HashMap<>(), false);
    }

    private void placeOrder(String number) {
        HashMap<String, String> params = new HashMap<>();
        params.put("authorizedOpeningTimeBegin", mOpenTimeStart);
        params.put("authorizedOpeningTimeEnd", mOpenTimeEnd);
        params.put("boxId", mTradeId);
        params.put("earningsLine", mEarningsLine);
        params.put("entrustTheDirection", mDirectionSelected);
        params.put("entrustTheHandCount", number);
        params.put("id", mID);
        params.put("lossLine", mLossLine);
        params.put("orders", mVariety);
        params.put("preOrderCloseDateBegin", mEqualTimeStart);
        params.put("preOrderCloseDateEnd", mEqualTimeEnd);
        params.put("token", User.getInstance().getToken());

        sendRequest(ManagementService.getInstance().placeOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetBoxInfo":
                if (head.isSuccess()) {
                    TradingBoxInfoVo tradingBoxInfoVo;

                    try {
                        tradingBoxInfoVo = (TradingBoxInfoVo) response;
                    } catch (Exception e) {
                        tradingBoxInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxInfoVo)
                        return;

                    mVariety = tradingBoxInfoVo.getVariety();
                    mID = tradingBoxInfoVo.getId();
                    mOpenTimeStart = tradingBoxInfoVo.getOpenPositionsTimeBegin();
                    mOpenTimeEnd = tradingBoxInfoVo.getOpenPositionsTimeEnd();
                    mEqualTimeStart = tradingBoxInfoVo.getClosePositionsTimeBegin();
                    mEqualTimeEnd = tradingBoxInfoVo.getClosePositionsTimeEnd();
                    mEarningsLine = tradingBoxInfoVo.getEarningsLine();
                    mLossLine = tradingBoxInfoVo.getLossLine();

                    String[] openTimeStart = mOpenTimeStart.split(" ");
                    String[] openTimeEnd = mOpenTimeEnd.split(" ");
                    String[] equalTimeStart = mEqualTimeStart.split(" ");
                    String[] equalTimeEnd = mEqualTimeEnd.split(" ");

                    mBinding.tvContract.setText(mVariety);
                    mBinding.tvOpenTimeStartDate.setText(openTimeStart[0].replace("-", "/"));
                    mBinding.tvOpenTimeStartHour.setText(openTimeStart[1]);
                    mBinding.tvOpenTimeEndDate.setText(openTimeEnd[0].replace("-", "/"));
                    mBinding.tvOpenTimeEndHour.setText(openTimeEnd[1]);
                    mBinding.tvEqualTimeStartDate.setText(equalTimeStart[0].replace("-", "/"));
                    mBinding.tvEqualTimeStartHour.setText(equalTimeStart[1]);
                    mBinding.tvEqualTimeEndDate.setText(equalTimeEnd[0].replace("-", "/"));
                    mBinding.tvEqualTimeEndHour.setText(equalTimeEnd[1]);
                    mBinding.etAmount.setText("1");
                    mBinding.etAmount.setSelection(1);
                    mBinding.tvProfit.setText(mEarningsLine);
                    mBinding.tvLoss.setText(mLossLine);
                }

                break;
            case "Account":
                if (head.isSuccess()) {
                    AccountVo accountVo;

                    try {
                        accountVo = (AccountVo) response;
                    } catch (Exception e) {
                        accountVo = null;

                        e.printStackTrace();
                    }

                    if (null == accountVo)
                        return;

                    mTransactionBalance = accountVo.getTransactionBalance();

                    getTenSpeedQuotes();
                }

                break;
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {
                    List<TenSpeedVo> tenSpeedVoList;

                    try {
                        tenSpeedVoList = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        tenSpeedVoList = null;

                        e.printStackTrace();
                    }

                    if (null == tenSpeedVoList || 0 == tenSpeedVoList.size())
                        return;

                    TenSpeedVo mTenSpeedVo = tenSpeedVoList.get(0);

                    if (null == mTenSpeedVo)
                        return;

                    mHighLimitPrice = mTenSpeedVo.getHighLimitPriceValue();

                    calculateMoneyEnough(mBinding.etAmount.getText().toString().trim());
                }

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
                        mTradeMessagePopUpWindow.showAtLocation(mBinding.tvBalanceMessage, Gravity.CENTER, 0, 0);
                    }
                }

                break;
            case "GetStatus":
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
                        mTradeMessagePopUpWindow.showAtLocation(mBinding.tvBalanceMessage, Gravity.CENTER, 0, 0);
                    }
                } else {
                    checkOrder();
                }

                break;
            case "CheckOrder":
                if (head.isSuccess()) {
                    OrderVo orderVo;

                    try {
                        orderVo = (OrderVo) response;
                    } catch (Exception e) {
                        orderVo = null;

                        e.printStackTrace();
                    }

                    if (null == orderVo) {
                        placeOrder(mBinding.etAmount.getText().toString());
                    } else {
                        int orderNum = orderVo.getOrderNum();

                        if (0 == orderNum) {
                            placeOrder(mBinding.etAmount.getText().toString());
                        } else {
                            if (null != mMessagePopupwindow && !mMessagePopupwindow.isShowing()) {
                                mMessagePopupwindow.setData(String.format(getString(R.string.trading_box_order_message), String.valueOf(orderNum)),
                                        getString(R.string.trading_box_order_continue), getString(R.string.trading_box_order_read),
                                        (view) -> {
                                            placeOrder(mBinding.etAmount.getText().toString());

                                            mMessagePopupwindow.dismiss();
                                        }, (view) -> {
                                            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXORDER).navigation();

                                            mMessagePopupwindow.dismiss();
                                        });
                                mMessagePopupwindow.showAtLocation(mBinding.tvBalanceMessage, Gravity.CENTER, 0, 0);
                            }
                        }
                    }

                }

                break;
            case "PlaceOrder":
                if (head.isSuccess())
                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXORDER).navigation();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickMore() {
            mDirectionSelected = "0";

            setEntrustDirectionLayout();
        }

        public void onClickEmpty() {
            mDirectionSelected = "1";

            setEntrustDirectionLayout();
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trading_box_number_error_zero);
                calculateMoneyEnough(amount);
            } else {
                String valueStr = String.valueOf(value);

                mBinding.etAmount.setText(valueStr);
                mBinding.etAmount.setSelection(valueStr.length());

                calculateMoneyEnough(valueStr);
            }
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();
            String valueStr = String.valueOf(value);

            mBinding.etAmount.setText(valueStr);
            mBinding.etAmount.setSelection(valueStr.length());

            calculateMoneyEnough(valueStr);
        }

        public void onClickEntrustOpenTimeTips() {
            if (null != mWindow && !mWindow.isShowing()) {
                mWindow.setData(getString(R.string.trading_box_entrust_open_time), new SpannableString(getString(R.string.trading_box_entrust_open_time_message)));
                mWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickEntrustEqualTimeTips() {
            if (null != mWindow && !mWindow.isShowing()) {
                mWindow.setData(getString(R.string.trading_box_entrust_equal_time), new SpannableString(getString(R.string.trading_box_entrust_equal_time_message)));
                mWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickMechanism() {
            if (null != mWindow && !mWindow.isShowing()) {
                mWindow.setData(getString(R.string.trading_box_entrust_mechanism), new SpannableString(getString(R.string.trading_box_entrust_mechanism_message)));
                mWindow.showAtLocation(mBinding.etAmount, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickOrderSubmit() {
            String amount = mBinding.etAmount.getText().toString().trim();

            if (TextUtils.isEmpty(amount))
                showShortToast(R.string.trading_box_entrust_amount_message);
            else if (!bEnoughFlag)
                showShortToast(R.string.trading_box_balance_not_enough_message);
            else
                getUserAddedServicesStatus();
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CAPITALTRANSFER)
                    .navigation();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

}
