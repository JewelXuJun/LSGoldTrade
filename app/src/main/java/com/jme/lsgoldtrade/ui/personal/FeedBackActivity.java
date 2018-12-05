package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityFeedbackBinding;

@Route(path = Constants.ARouterUriConst.FEEDBACK)
public class FeedBackActivity extends JMEBaseActivity {

    private ActivityFeedbackBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityFeedbackBinding) mBindingUtil;

        initToolbar(R.string.personal_feedback, true);

        setRightNavigation(getString(R.string.personal_feedback_send), 0, R.style.ToolbarThemeBlue, null);
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
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
