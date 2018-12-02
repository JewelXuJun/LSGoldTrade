package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketBinding;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity {

    private ActivityMarketBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMarketBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(this, true, R.color.common_font_stable);
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

        }

    }
}
