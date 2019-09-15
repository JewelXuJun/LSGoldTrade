package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityThawBinding;

/**
 * 申请解冻
 */
@Route(path = Constants.ARouterUriConst.THAW)
public class ThawActivity extends JMEBaseActivity {

    private ActivityThawBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_thaw;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("申请解冻", true);
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

        mBinding = (ActivityThawBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickAll() {

        }

        public void onClickThaw() {

        }
    }
}
