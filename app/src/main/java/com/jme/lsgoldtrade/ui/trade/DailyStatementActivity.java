package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityDailyStatementBinding;

@Route(path = Constants.ARouterUriConst.DAILYSTATEMENT)
public class DailyStatementActivity extends JMEBaseActivity {

    private ActivityDailyStatementBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_daily_statement;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityDailyStatementBinding) mBindingUtil;

        initToolbar(R.string.trade_daily_statement_query, true);
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

        public void onClickSelectDate() {

        }

    }
}
