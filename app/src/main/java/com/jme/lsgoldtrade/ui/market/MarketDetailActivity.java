package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailBinding;
import com.jme.lsgoldtrade.service.MarketService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity {

    private ActivityMarketDetailBinding mBinding;

    private MarketOrderPopUpWindow mPopupWindow;

    private String mMarket;

    private static final String DIRECTION_AFTER = "1";

    private static final String COUNT_TCHART = "660";
    private static final String COUNT_KCHART = "200";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMarketDetailBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(this, true, R.color.common_font_stable);

        mPopupWindow = new MarketOrderPopUpWindow(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mMarket = getIntent().getStringExtra("Market");
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getTenSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", mMarket);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getTChartQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mMarket);
        params.put("qryFlag", DIRECTION_AFTER);
        params.put("count", COUNT_TCHART);

        sendRequest(MarketService.getInstance().getTChartQuotes, params, false, false, false);
    }

    private void getKChartQuotes(String type, String time, String flag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("contractId", mMarket);
        params.put("quoteTime", time);
        params.put("qryFlag", flag);
        params.put("count", COUNT_KCHART);

        sendRequest(MarketService.getInstance().getKChartQuotes, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {

                } else {

                }

                break;
            case "GetTChartQuotes":
                if (head.isSuccess()) {

                } else {

                }

                break;
            case "GetKChartQuotes":
                if (head.isSuccess()) {

                } else {

                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickBack() {
            finish();
        }

        public void onClickDeclarationForm() {

        }

        public void onClickOneKeyOrder() {
            mPopupWindow.showAtLocation(mBinding.layoutFooterview, Gravity.BOTTOM, 0, 0);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mPopupWindow && mPopupWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (null != mPopupWindow && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            else
                finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
