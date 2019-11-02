package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityConditionSheetExplainBinding;

@Route(path = Constants.ARouterUriConst.CONDITIONSHEETEXPLAIN)
public class ConditionSheetExplainActivity extends JMEBaseActivity {

    private ActivityConditionSheetExplainBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_condition_sheet_explain;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_condition_sheet_explain, true);
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

        mBinding = (ActivityConditionSheetExplainBinding) mBindingUtil;
    }

}
