package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailBinding;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity {

    private ActivityMarketDetailBinding mBinding;

    private MarketOrderPopUpWindow mPopupWindow;

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mPopupWindow && mPopupWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
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
