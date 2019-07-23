package com.jme.lsgoldtrade.ui.tradingbox;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityPlaceOrderBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.TradingBoxInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.util.SpanUtils;
import com.jme.lsgoldtrade.util.TradeBoxFunctionUtils;
import com.jme.lsgoldtrade.view.FuDongJiZhiPopupWindow;
import com.jme.lsgoldtrade.view.HasProfitLossRiskSignPopupWindow;
import com.suke.widget.SwitchButton;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 预埋单
 */
@Route(path = Constants.ARouterUriConst.PLACEORDER)
public class PlaceOrderActivity extends JMEBaseActivity {

    private ActivityPlaceOrderBinding mBinding;
    private String type, id, direction;
    public String isMore = "";
    private HasProfitLossRiskSignPopupWindow mWindow;
    private FuDongJiZhiPopupWindow fuDongJiZhiPopupWindow;
    private String openPositionsTimeBegin = "";
    private String openPositionsTimeEnd = "";
    private String closePositionsTimeBegin = "";
    private String closePositionsTimeEnd = "";
    private String id1;
    private String variety;
    private String openPrice;
    public int chooseType = -1;
    private float mPriceMove = 0.00f;
    private String earningsLine;
    private String lossLine;
    private long availableFunds;
    public boolean enoughPrice = false;
    private String highLimitPrice;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_place_order;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityPlaceOrderBinding) mBindingUtil;
        initToolbar("预埋单", true);
        setRightNavigation();
        mWindow = new HasProfitLossRiskSignPopupWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
        fuDongJiZhiPopupWindow = new FuDongJiZhiPopupWindow(mContext);
        fuDongJiZhiPopupWindow.setOutsideTouchable(true);
        fuDongJiZhiPopupWindow.setFocusable(true);
    }

    private void setRightNavigation() {
        setRightNavigation("", R.mipmap.ic_more, 0, () -> {
            TradeBoxFunctionUtils.show(this, "");
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        type = getIntent().getStringExtra("Type");
        id = getIntent().getStringExtra("TradeId");
        direction = getIntent().getStringExtra("Direction");
        if ("0".equals(type)) {
            mBinding.more.setVisibility(View.VISIBLE);
            mBinding.kong.setVisibility(View.VISIBLE);
            mBinding.chooseDirection.setVisibility(View.GONE);
            mBinding.more.setBackground(getResources().getDrawable(R.drawable.choose_more));
            mBinding.kong.setBackground(getResources().getDrawable(R.drawable.bg_btn_kong_solid));
            mBinding.more.setTextColor(getResources().getColor(R.color.white));
            mBinding.kong.setTextColor(getResources().getColor(R.color.color_green));
            isMore = "0";
        } else if ("1".equals(type)) {
            mBinding.more.setVisibility(View.VISIBLE);
            mBinding.kong.setVisibility(View.VISIBLE);
            mBinding.chooseDirection.setVisibility(View.GONE);
            mBinding.more.setBackground(getResources().getDrawable(R.drawable.bg_btn_more_solid));
            mBinding.kong.setBackground(getResources().getDrawable(R.drawable.choose_kong));
            mBinding.more.setTextColor(getResources().getColor(R.color.color_red));
            mBinding.kong.setTextColor(getResources().getColor(R.color.white));
            isMore = "1";
        } else if ("2".equals(type)) {
            if ("0".equals(direction)) {
                mBinding.more.setVisibility(View.GONE);
                mBinding.kong.setVisibility(View.GONE);
                mBinding.chooseDirection.setVisibility(View.VISIBLE);
                mBinding.chooseDirection.setText("多");
                mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_red));
                isMore = "0";
            } else {
                mBinding.more.setVisibility(View.GONE);
                mBinding.kong.setVisibility(View.GONE);
                mBinding.chooseDirection.setVisibility(View.VISIBLE);
                mBinding.chooseDirection.setText("空");
                mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_green));
                isMore = "1";
            }

        } else if ("3".equals(type)) {
            if ("0".equals(direction)) {
                mBinding.more.setVisibility(View.GONE);
                mBinding.kong.setVisibility(View.GONE);
                mBinding.chooseDirection.setVisibility(View.VISIBLE);
                mBinding.chooseDirection.setText("多");
                mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_red));
                isMore = "0";
            } else {
                mBinding.more.setVisibility(View.GONE);
                mBinding.kong.setVisibility(View.GONE);
                mBinding.chooseDirection.setVisibility(View.VISIBLE);
                mBinding.chooseDirection.setText("空");
                mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_green));
                isMore = "1";
            }
        }

        mBinding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                moneyEnough(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        mBinding.yinglipingcangxianjiage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.yinglipingcangxianjiage.setText(s);
                        mBinding.yinglipingcangxianjiage.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.yinglipingcangxianjiage.setText(s);
                    mBinding.yinglipingcangxianjiage.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.yinglipingcangxianjiage.setText(s.subSequence(0, 1));
                        mBinding.yinglipingcangxianjiage.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.kuisunpingcangxianjiage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.yinglipingcangxianjiage.setText(s);
                        mBinding.yinglipingcangxianjiage.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.yinglipingcangxianjiage.setText(s);
                    mBinding.yinglipingcangxianjiage.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.yinglipingcangxianjiage.setText(s.subSequence(0, 1));
                        mBinding.yinglipingcangxianjiage.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getDataFromNet();
    }

    private void getDataFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        sendRequest(ManagementService.getInstance().tradingBoxInfo, params, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContractInfo();
    }

    private void getPrice() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", AppConfig.Select_ContractId);
        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getContractInfo() {
        if (mUser != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("accountId", mUser.getAccountID());

            sendRequest(TradeService.getInstance().account, params, false);
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "TradingBoxInfo":
                if (head.isSuccess()) {
                    TradingBoxInfoVo value;
                    try {
                        value = (TradingBoxInfoVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }

                    id1 = value.getId();
                    openPrice = value.getOpenPrice();
                    earningsLine = value.getEarningsLine();
                    lossLine = value.getLossLine();

                    mBinding.yinglipingchagnxian.setText("盈利平仓线     " + earningsLine);
                    mBinding.kuisunpingcangxian.setText("亏损平仓线     " + lossLine);
//                    if ("0".equals(openPrice)) {
//                        mBinding.weituokaicangjiage.setVisibility(View.GONE);
//                        mBinding.chooseType.setVisibility(View.GONE);
//                        mBinding.showprice.setVisibility(View.GONE);
//                        mBinding.shownum.setVisibility(View.VISIBLE);
//                    } else {
//                        mBinding.weituokaicangjiage.setVisibility(View.VISIBLE);
//                        mBinding.etPrice.setText(openPrice);
//                        mBinding.chooseType.setVisibility(View.VISIBLE);
//                        mBinding.showprice.setVisibility(View.VISIBLE);
//                        mBinding.shownum.setVisibility(View.GONE);
//                    }

                    variety = value.getVariety();
                    openPositionsTimeBegin = value.getOpenPositionsTimeBegin();
                    openPositionsTimeEnd = value.getOpenPositionsTimeEnd();

                    closePositionsTimeBegin = value.getClosePositionsTimeBegin();
                    closePositionsTimeEnd = value.getClosePositionsTimeEnd();

                    String[] openBegin = openPositionsTimeBegin.split(" ");
                    String[] openEnd = openPositionsTimeEnd.split(" ");
                    String[] closeBegin = closePositionsTimeBegin.split(" ");
                    String[] closeEnd = closePositionsTimeEnd.split(" ");
                    mBinding.kaicangbeginyear.setText(openBegin[0]);
                    mBinding.kaicangbeginclock.setText(openBegin[1]);
                    mBinding.kaicangcloseyear.setText(openEnd[0]);
                    mBinding.kaicangcloseclock.setText(openEnd[1]);
                    mBinding.pingcangbeginyear.setText(closeBegin[0]);
                    mBinding.pingcangbeginclock.setText(closeBegin[1]);
                    mBinding.pingcangendyear.setText(closeEnd[0]);
                    mBinding.pingcangendclock.setText(closeEnd[1]);
                    mBinding.pinzhong.setText(variety);

                    if ("Ag(T+D)".equals(variety)) {
                        mPriceMove = new BigDecimal(100).divide(new BigDecimal(100)).floatValue();
                    } else {
                        mPriceMove = new BigDecimal(1).divide(new BigDecimal(100)).floatValue();
                    }
                }
                break;
            case "HasProfitLossRiskSign":
                if (head.isSuccess()) {
                    mBinding.yingkuisetting.setVisibility(View.VISIBLE);
                } else {
                    if (null != mWindow) {
                        mWindow.setData(
                                (View) -> {
                                    mWindow.dismiss();
                                },(View) -> {
                                    if (mWindow.isAgree == 1) {
                                        mWindow.dismiss();
                                        //获取用户是否同意盈亏设置协议
                                        sendRequest(ManagementService.getInstance().agree, new HashMap<>(), false);
                                    } else {
                                        showShortToast("请阅读该协议并同意");
                                    }
                                });
                        mWindow.showAtLocation(mBinding.pingcangendclock, Gravity.CENTER, 0, 0);
                    }
                }
                break;
            case "SubmitTradingBox":
                if (head.isSuccess()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.TRADINGBOXORDER)
                            .navigation();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
            case "Agree":
                if (head.isSuccess()) {
//                    if ("0".equals(openPrice)) {
//                        mBinding.weituokaicangjiage.setVisibility(View.GONE);
//                        mBinding.chooseType.setVisibility(View.GONE);
//                        mBinding.showprice.setVisibility(View.GONE);
//                        mBinding.shownum.setVisibility(View.VISIBLE);
//                    } else {
//                        mBinding.weituokaicangjiage.setVisibility(View.VISIBLE);
//                        mBinding.etPrice.setText(openPrice);
//                        mBinding.chooseType.setVisibility(View.VISIBLE);
//                        mBinding.showprice.setVisibility(View.VISIBLE);
//                        mBinding.shownum.setVisibility(View.GONE);
//                    }
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

                    String lastSettlePrice = mTenSpeedVo.getLastSettlePrice();
                    highLimitPrice = mTenSpeedVo.getHighLimitPrice();
                    String mLowerLimitPrice = mTenSpeedVo.getLowerLimitPrice();
                    String mHighLimitPrice = mTenSpeedVo.getHighLimitPrice();
                    String num = mBinding.etAmount.getText().toString().trim();

                    moneyEnough(num);
                }
                break;
            case "Account":
                if (head.isSuccess()) {
                    AccountVo mAccountVo;
                    try {
                        mAccountVo = (AccountVo) response;
                    } catch (Exception e) {
                        mAccountVo = null;

                        e.printStackTrace();
                    }

                    if (null == mAccountVo)
                        return;

                    availableFunds = mAccountVo.getTransactionBalance();
                    getPrice();

//                    mBinding.tvGuaranteeFund.setText(MarketUtil.decimalFormatMoney(mAccountVo.getMinReserveFundStr()));
//                    mBinding.tvAvailableFunds.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(availableFunds)));

                }

                break;
        }
    }

    private void moneyEnough(String num) {
        int tradeUnit = 0;
        double marginRatio = 0;

        if (mBinding.pinzhong.getText().toString().trim().equals("Au(T+D)")) {
            tradeUnit = 1000;
            marginRatio = 0.09;
        } else if (mBinding.pinzhong.getText().toString().trim().equals("mAu(T+D)")) {
            tradeUnit = 100;
            marginRatio = 0.09;
        } else if (mBinding.pinzhong.getText().toString().trim().equals("Ag(T+D)")){
            tradeUnit = 1;
            marginRatio = 0.1;
        }

        float amout = (float) (Float.parseFloat(highLimitPrice) * tradeUnit * marginRatio * Float.parseFloat(num));

        if (amout > (availableFunds / 100)) {
            enoughPrice = false;
            mBinding.enoughPrice.setText(new SpanUtils(this)
                    .append("预计可用金额不足，请先")
                    .setForegroundColor(getResources().getColor(R.color.black))
                    .append("入金")
                    .setForegroundColor(getResources().getColor(R.color.color_blue_deep))
                    .create());
        } else {
            enoughPrice = true;
            mBinding.enoughPrice.setText("可用资金:" + (availableFunds / 100));
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
//        swichBtnStatus(mBinding.yinkuisetting);
//        mBinding.yinkuisetting.setChecked(false);
//        mBinding.yinkuisetting.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (isChecked) {
////                    mBinding.yinkuisetting.setChecked(false);
//                    //获取用户是否同意盈亏设置协议
//                    sendRequest(ManagementService.getInstance().hasProfitLossRiskSign, new HashMap<>(), false);
//                } else {
//                    mBinding.yingkuisetting.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    private void swichBtnStatus(SwitchButton switchButton) {
        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();
        switchButton.toggle(false);
        switchButton.setShadowEffect(true);
        switchButton.setEnabled(true);
        switchButton.setEnableEffect(true);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickPrice() {
            mBinding.showprice.setVisibility(View.VISIBLE);
            mBinding.shownum.setVisibility(View.GONE);
            mBinding.price.setBackground(getResources().getDrawable(R.drawable.drawable_price));
            mBinding.num.setBackground(null);
            chooseType = 0;
        }

        public void onClickNum() {
            mBinding.showprice.setVisibility(View.GONE);
            mBinding.shownum.setVisibility(View.VISIBLE);
            mBinding.price.setBackground(null);
            mBinding.num.setBackground(getResources().getDrawable(R.drawable.drawable_num));
            chooseType = 1;
        }

        public void onClickMore() {
            mBinding.more.setBackground(getResources().getDrawable(R.drawable.choose_more));
            mBinding.kong.setBackground(getResources().getDrawable(R.drawable.bg_btn_kong_solid));
            mBinding.more.setTextColor(getResources().getColor(R.color.white));
            mBinding.kong.setTextColor(getResources().getColor(R.color.color_green));
            isMore = "0";
        }

        public void onClickKong() {
            mBinding.more.setBackground(getResources().getDrawable(R.drawable.bg_btn_more_solid));
            mBinding.kong.setBackground(getResources().getDrawable(R.drawable.choose_kong));
            mBinding.more.setTextColor(getResources().getColor(R.color.color_red));
            mBinding.kong.setTextColor(getResources().getColor(R.color.white));
            isMore = "1";
        }

        public void onClickRuJin() {
            if (enoughPrice == false)
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.CAPITALTRANSFER)
                            .navigation();
        }

        public void onClickPriceMinus() {
            //委托开仓价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.etPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickPriceAdd() {
            //委托开仓价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.etPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etPrice.setText(valueStr);
            mBinding.etPrice.setSelection(valueStr.length());

//            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mHighLimitPrice)) == 1) {
//                showShortToast(R.string.trade_limit_up_price_error);
//
//                mBinding.etPrice.setText(price);
//                mBinding.etPrice.setSelection(price.length());
//            } else {
//                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);
//
//                mBinding.etPrice.setText(valueStr);
//                mBinding.etPrice.setSelection(valueStr.length());
//            }
        }

        public void onClickNumMinus() {
            //委托开仓手数
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";
            moneyEnough(amount);
            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickNumAdd() {
            //委托开仓手数
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";
            moneyEnough(amount);
            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickYingliPriceMinus() {
            //盈利平仓线价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.yinglipingcangxianjiage);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.yinglipingcangxianjiage.setText(price);
                mBinding.yinglipingcangxianjiage.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.yinglipingcangxianjiage.setText(valueStr);
                mBinding.yinglipingcangxianjiage.setSelection(valueStr.length());
            }
        }

        public void onClickYingliPriceAdd() {
            //盈利平仓线价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.yinglipingcangxianjiage);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.yinglipingcangxianjiage.setText(valueStr);
            mBinding.yinglipingcangxianjiage.setSelection(valueStr.length());
        }

        public void onClickKuiSunPriceMinus() {
            //亏损平仓线价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.kuisunpingcangxianjiage);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.kuisunpingcangxianjiage.setText(price);
                mBinding.kuisunpingcangxianjiage.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.kuisunpingcangxianjiage.setText(valueStr);
                mBinding.kuisunpingcangxianjiage.setSelection(valueStr.length());
            }
        }

        public void onClickKuiSunPriceAdd() {
            //亏损平仓线价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.kuisunpingcangxianjiage);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.kuisunpingcangxianjiage.setText(valueStr);
            mBinding.kuisunpingcangxianjiage.setSelection(valueStr.length());
        }

        public void onClickYingLiNumMinus() {
            //盈利平仓线点数
            hiddenKeyBoard();

            String amount = mBinding.yinglipingcangxiandianshu.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.yinglipingcangxiandianshu.setText(String.valueOf(value));
            }
        }

        public void onClickYingLiNumAdd() {
            hiddenKeyBoard();

            String amount = mBinding.yinglipingcangxiandianshu.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.yinglipingcangxiandianshu.setText(String.valueOf(value));
            }
        }

        public void onClickKuiSunNumMinus() {
            //亏损平仓线手数
            hiddenKeyBoard();

            String amount = mBinding.kuisunpingcangxiandianshu.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.kuisunpingcangxiandianshu.setText(String.valueOf(value));
            }
        }

        public void onClickKuiSunNumAdd() {
            //亏损平仓线手数
            hiddenKeyBoard();

            String amount = mBinding.kuisunpingcangxiandianshu.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (value < 1) {
                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.kuisunpingcangxiandianshu.setText(String.valueOf(value));
            }
        }

        public void onClickFuDongJiZhi() {
            String title = "浮动成交机制";
            String content = "浮动成交机制将按照市价FAK指令下单，实现快速成交。根据相关交易规则,市价FAK指令可能会导致成交价格与委托价格不一致（市价报单默认交易品种的涨停价为委托价，委托时按照委托价冻结保证金，实际成交后按照成交价调整保证金）。盈利平仓线和亏损平仓线设置的点数是基于成交价格上涨/下跌的点数，黄金品种1个点代表0.01元，白银品种1个点代表1元。";

            if (null != fuDongJiZhiPopupWindow) {
                fuDongJiZhiPopupWindow.setData(title, content,
                        (View) -> {
                            fuDongJiZhiPopupWindow.dismiss();
                        });
                fuDongJiZhiPopupWindow.showAtLocation(mBinding.pingcangendclock, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickKaiCangShiJian() {
            String title = "委托开仓时间段";
            String content = "系统将在委托开仓时间段内的任意交易时间为您执行开仓指令。";

            if (null != fuDongJiZhiPopupWindow) {
                fuDongJiZhiPopupWindow.setData(title, content,
                        (View) -> {
                            fuDongJiZhiPopupWindow.dismiss();
                        });
                fuDongJiZhiPopupWindow.showAtLocation(mBinding.pingcangendclock, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickPingCangShiJian() {
            String title = "委托平仓时间段";
            String content = "若建仓成功后在委托平仓时间开始之前未触发盈利/亏损平仓线，系统将在委托平仓时间内为您执行平仓指令。";

            if (null != fuDongJiZhiPopupWindow) {
                fuDongJiZhiPopupWindow.setData(title, content,
                        (View) -> {
                            fuDongJiZhiPopupWindow.dismiss();
                        });
                fuDongJiZhiPopupWindow.showAtLocation(mBinding.pingcangendclock, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickOrderSubmit() {
            String etNum = mBinding.etAmount.getText().toString().trim();
            if (TextUtils.isEmpty(etNum)) {
                showShortToast("请输入委托手数");
                return;
            }

            if (TextUtils.isEmpty(isMore)) {
                showShortToast("请选择委托方向");
                return;
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("authorizedOpeningTimeBegin", openPositionsTimeBegin);
            params.put("authorizedOpeningTimeEnd", openPositionsTimeEnd);
            params.put("boxId", id);
            params.put("earningsLine", earningsLine);
            params.put("entrustTheDirection", isMore);
            params.put("entrustTheHandCount", etNum);
            params.put("id", id1);
            params.put("lossLine", lossLine);
            params.put("orders", variety);
            params.put("preOrderCloseDateBegin", closePositionsTimeBegin);
            params.put("preOrderCloseDateEnd", closePositionsTimeEnd);
            params.put("token", User.getInstance().getToken());
            sendRequest(ManagementService.getInstance().submitTradingBox, params, false);
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private String getPrice(EditText editText) {
        String price = editText.getText().toString();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }
}
