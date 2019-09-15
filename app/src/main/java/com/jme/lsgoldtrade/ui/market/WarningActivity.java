package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWarningBinding;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.WarnVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 预警
 */
@Route(path = Constants.ARouterUriConst.WARNING)
public class WarningActivity extends JMEBaseActivity {

    private ActivityWarningBinding mBinding;

    private float mPriceMove = 0.00f;
    private String mContractID;
    private String mLatestPrice;
    private String circle = "1";
    private String upPriceIsOpen = "0";
    private String lowPriceIsOpen = "0";
    private String id = "";
    private boolean bFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_UARNING_PDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_UARNING_PDATE_DATA);

                    getTenSpeedQuotes();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UARNING_PDATE_DATA, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_warning;
    }

    @Override
    protected void initView() {
        super.initView();

        mContractID = getIntent().getStringExtra("ContractID");

        if ("Ag(T+D)".equals(mContractID))
            mPriceMove = new BigDecimal(100).divide(new BigDecimal(100)).floatValue();
        else
            mPriceMove = new BigDecimal(1).divide(new BigDecimal(100)).floatValue();

        mBinding.type.setText(mContractID);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initToolbar("预警设置", true);
        setRightNavigation("保存", 0, R.style.ToolbarThemeBlue, () -> {
            //保存接口
            isUpData();
        });
        getWarmFromNet();
    }

    @Override
    protected void onResume() {
        super.onResume();

        bFlag = true;

        getTenSpeedQuotes();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_UARNING_PDATE_DATA);
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }


    private void getWarmFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mContractID);
        sendRequest(TradeService.getInstance().warnInfo, params, true);
    }

    private void getTenSpeedQuotes() {
        if (TextUtils.isEmpty(mContractID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("list", mContractID);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.etCeilingPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etCeilingPrice.setText(s);
                        mBinding.etCeilingPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etCeilingPrice.setText(s);
                    mBinding.etCeilingPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etCeilingPrice.setText(s.subSequence(0, 1));
                        mBinding.etCeilingPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.etFloorPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etFloorPrice.setText(s);
                        mBinding.etFloorPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etFloorPrice.setText(s);
                    mBinding.etFloorPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etFloorPrice.setText(s.subSequence(0, 1));
                        mBinding.etFloorPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.cb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.cb1.setChecked(true);
                mBinding.cb3.setChecked(false);
                mBinding.cb7.setChecked(false);
                circle = "1";
            }
        });
        mBinding.cb3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.cb1.setChecked(false);
                mBinding.cb3.setChecked(true);
                mBinding.cb7.setChecked(false);
                circle = "2";
            }
        });
        mBinding.cb7.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.cb1.setChecked(false);
                mBinding.cb3.setChecked(false);
                mBinding.cb7.setChecked(true);
                circle = "3";
            }
        });
        mBinding.cbCeiling.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                upPriceIsOpen = "1";
            } else {
                upPriceIsOpen = "0";
            }
        });
        mBinding.cbFloor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lowPriceIsOpen = "1";
            } else {
                lowPriceIsOpen = "0";
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityWarningBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickUpMinus() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etCeilingPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etCeilingPrice.setText(price);
                mBinding.etCeilingPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etCeilingPrice.setText(valueStr);
                mBinding.etCeilingPrice.setSelection(valueStr.length());
            }
        }

        public void onClickUpPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etCeilingPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etCeilingPrice.setText(valueStr);
            mBinding.etCeilingPrice.setSelection(valueStr.length());
        }

        public void onClickDownMinus() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etFloorPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etFloorPrice.setText(price);
                mBinding.etFloorPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etFloorPrice.setText(valueStr);
                mBinding.etFloorPrice.setSelection(valueStr.length());
            }
        }

        public void onClickDownPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etFloorPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etFloorPrice.setText(valueStr);
            mBinding.etFloorPrice.setSelection(valueStr.length());
        }

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

                    TenSpeedVo tenSpeedVo = list.get(0);

                    if (null == tenSpeedVo)
                        return;

                    mLatestPrice = tenSpeedVo.getLatestPrice();
                    String upDown = tenSpeedVo.getUpDown();
                    int rateType;

                    if (TextUtils.isEmpty(upDown))
                        rateType = 0;
                    else
                        rateType = new BigDecimal(upDown).compareTo(new BigDecimal(0));

                    mBinding.price.setText(mLatestPrice);
                    mBinding.price.setTextColor(ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)));
                    mBinding.bili.setText(MarketUtil.getMarketRateValue(rateType, tenSpeedVo.getUpDownRate()));
                    mBinding.bili.setTextColor(ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)));

                    String priceUp = mBinding.etCeilingPrice.getText().toString();
                    String priceDown = mBinding.etFloorPrice.getText().toString();

                    if (TextUtils.isEmpty(priceUp))
                        mBinding.etCeilingPrice.setText(mLatestPrice);

                    if (TextUtils.isEmpty(priceDown))
                        mBinding.etFloorPrice.setText(mLatestPrice);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UARNING_PDATE_DATA, getTimeInterval());
                }

                break;
            case "SetWarn":
                if (head.isSuccess()) {
                    showShortToast("设置成功");
                    finish();
                }

                break;
            case "WarnInfo":
                if (head.isSuccess()) {
                    WarnVo warnVo;
                    try {
                        warnVo = (WarnVo) response;
                    } catch (Exception e) {
                        warnVo = null;
                        e.printStackTrace();
                    }

                    if (warnVo == null) {
                        mBinding.cbCeiling.setChecked(false);
                        mBinding.cbFloor.setChecked(false);

                        return;
                    }

                    setWarnData(warnVo);
                }

                break;
        }
    }

    private void setWarnData(WarnVo warnVo) {
        id = "" + warnVo.getId();
        if (warnVo.getCeilingFlag().equals("1")) {
            mBinding.cbCeiling.setChecked(true);
            mBinding.etCeilingPrice.setText(BigDecimalUtil.formatMoney(new BigDecimal(warnVo.getPriceCeiling()).divide(new BigDecimal(100)).toPlainString()));
        } else {
            mBinding.cbCeiling.setChecked(false);
            mBinding.etCeilingPrice.setText(mLatestPrice);
        }
        if (warnVo.getFloorFlag().equals("1")) {
            mBinding.cbFloor.setChecked(true);
            mBinding.etFloorPrice.setText(BigDecimalUtil.formatMoney(new BigDecimal(warnVo.getPriceFloor()).divide(new BigDecimal(100)).toPlainString()));
        } else {
            mBinding.cbFloor.setChecked(false);
            mBinding.etFloorPrice.setText(mLatestPrice);
        }
        switch (warnVo.getCycle()) {
            case "1":
                mBinding.cb1.setChecked(true);
                break;
            case "2":
                mBinding.cb3.setChecked(true);
                break;
            case "3":
                mBinding.cb7.setChecked(true);
                break;
        }
    }

    private void isUpData() {
        String upPrice = mBinding.etCeilingPrice.getText().toString().trim();
        String lowPrice = mBinding.etFloorPrice.getText().toString().trim();

        if (upPriceIsOpen.equals("0") && lowPriceIsOpen.equals("0")) {
            showShortToast("请选择价格上限或者下限");
            return;
        } else if (upPriceIsOpen.equals("1") && TextUtils.isEmpty(upPrice)) {
            showShortToast("请输入上限价格");
            return;
        } else if (lowPriceIsOpen.equals("1") && TextUtils.isEmpty(lowPrice)) {
            showShortToast("请输入下限价格");
            return;
        } else if (upPriceIsOpen.equals("1") && lowPriceIsOpen.equals("1") && new BigDecimal(upPrice).compareTo(new BigDecimal(lowPrice)) <= 0) {
            showShortToast("请输入正确的价格上限和价格下限");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("contractId", mContractID);
        params.put("priceCeiling", BigDecimalUtil.formatStrNumber(new BigDecimal(upPrice).multiply(new BigDecimal(100)).toPlainString()));
        params.put("priceFloor", BigDecimalUtil.formatStrNumber(new BigDecimal(lowPrice).multiply(new BigDecimal(100)).toPlainString()));
        params.put("ceilingFlag", upPriceIsOpen);
        params.put("floorFlag", lowPriceIsOpen);
        params.put("cycle", circle);
        sendRequest(TradeService.getInstance().setWarn, params, true);
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etCeilingPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
