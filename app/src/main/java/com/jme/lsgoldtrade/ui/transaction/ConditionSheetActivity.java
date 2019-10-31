package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityConditionSheetBinding;

@Route(path = Constants.ARouterUriConst.CONDITIONSHEET)
public class ConditionSheetActivity extends JMEBaseActivity {

    private ActivityConditionSheetBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_condition_sheet;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_condition_sheet, true);

        setRightNavigation(getResources().getString(R.string.transaction_explain), 0, R.style.ToolbarThemeBlue, null);
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

        mBinding = (ActivityConditionSheetBinding) mBindingUtil;
    }

}
