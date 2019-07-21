package com.jme.lsgoldtrade.ui.market;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityYujingBinding;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 预警
 */
@Route(path = Constants.ARouterUriConst.YUJING)
public class YujingActivity extends JMEBaseActivity {

    private ActivityYujingBinding mBinding;

    private float mPriceMove = 0.00f;

    private String type, price, range, rate = "";

    private String circle = "1";

    private String upPriceIsOpen = "0";

    private String lowPriceIsOpen = "0";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_yujing;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityYujingBinding) mBindingUtil;
        type = getIntent().getStringExtra("type");
        price = getIntent().getStringExtra("price");
        range = getIntent().getStringExtra("range");
        rate = getIntent().getStringExtra("rate");

        if ("Ag(T+D)".equals(type)) {
            mPriceMove = new BigDecimal(100).divide(new BigDecimal(100)).floatValue();
        } else {
            mPriceMove = new BigDecimal(1).divide(new BigDecimal(100)).floatValue();
        }

        mBinding.type.setText(type);
        mBinding.price.setText(price);
        mBinding.bili.setText(rate);
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

    private void getWarmFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", type);
        sendRequest(TradeService.getInstance().warnInfo, params, true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.etUpPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etUpPrice.setText(s);
                        mBinding.etUpPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etUpPrice.setText(s);
                    mBinding.etUpPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etUpPrice.setText(s.subSequence(0, 1));
                        mBinding.etUpPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.etLowPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etLowPrice.setText(s);
                        mBinding.etLowPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etLowPrice.setText(s);
                    mBinding.etLowPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etLowPrice.setText(s.subSequence(0, 1));
                        mBinding.etLowPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.cb1.setChecked(true);
                    mBinding.cb3.setChecked(false);
                    mBinding.cb7.setChecked(false);
                    circle = "1";
                }
            }
        });
        mBinding.cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.cb1.setChecked(false);
                    mBinding.cb3.setChecked(true);
                    mBinding.cb7.setChecked(false);
                    circle = "3";
                }
            }
        });
        mBinding.cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.cb1.setChecked(false);
                    mBinding.cb3.setChecked(false);
                    mBinding.cb7.setChecked(true);
                    circle = "7";
                }
            }
        });
        mBinding.cbUpprice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    upPriceIsOpen = "1";
                } else {
                    upPriceIsOpen = "0";
                }
            }
        });
        mBinding.cbLowprice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lowPriceIsOpen = "1";
                } else {
                    lowPriceIsOpen = "0";
                }
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickUpMinus() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etUpPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etUpPrice.setText(price);
                mBinding.etUpPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etUpPrice.setText(valueStr);
                mBinding.etUpPrice.setSelection(valueStr.length());
            }
        }

        public void onClickUpPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etUpPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etUpPrice.setText(valueStr);
            mBinding.etUpPrice.setSelection(valueStr.length());
        }

        public void onClickDownMinus() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etLowPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                showShortToast(R.string.trade_limit_down_price_error);

                mBinding.etLowPrice.setText(price);
                mBinding.etLowPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etLowPrice.setText(valueStr);
                mBinding.etLowPrice.setSelection(valueStr.length());
            }
        }

        public void onClickDownPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice(mBinding.etLowPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etLowPrice.setText(valueStr);
            mBinding.etLowPrice.setSelection(valueStr.length());
        }

    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "SetWarm":
                if (head.isSuccess()) {
                    showShortToast(head.getMsg());
                    AppManager.getAppManager().finishActivity();
//                    tipUser();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
            case "GetWarm":
                if (head.isSuccess()) {

                }
                break;
        }
    }

    private void isUpData() {
        String upPrice = mBinding.etUpPrice.getText().toString().trim().replace(".", "");
        String lowPrice = mBinding.etLowPrice.getText().toString().trim().replace(".", "");
        if (TextUtils.isEmpty(upPrice)) {
            showShortToast("请输入价格上限");
            return;
        }
        if (TextUtils.isEmpty(lowPrice)) {
            showShortToast("请输入价格下限");
            return;
        }
        if (upPriceIsOpen.equals("0")) {
            showShortToast("请勾选价格上限");
            return;
        }
        if (lowPriceIsOpen.equals("0")) {
            showShortToast("请勾选价格下限");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", type);
        params.put("priceCeiling", Long.parseLong(upPrice) * 100 + "");
        params.put("priceFloor", Long.parseLong(lowPrice) * 100 + "");
        params.put("ceilingFlag", upPriceIsOpen);
        params.put("floorFlag", lowPriceIsOpen);
        params.put("cycle", circle);
        sendRequest(TradeService.getInstance().setWarm, params, true);
    }

    private void tipUser() {
        View v = View.inflate(this, R.layout.yujing_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(this, R.style.dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView price = (TextView) v.findViewById(R.id.price);
        TextView upPrice = (TextView) v.findViewById(R.id.upPrice);
        TextView details = (TextView) v.findViewById(R.id.details);
        TextView ikown = (TextView) v.findViewById(R.id.ikown);
        dialog.setCancelable(true);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppManager.getAppManager().finishActivity();
            }
        });
        ikown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etUpPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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