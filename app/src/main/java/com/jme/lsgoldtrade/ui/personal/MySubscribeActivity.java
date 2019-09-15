package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMySubscribeBinding;

/**
 * 我的订阅
 */
@Route(path = Constants.ARouterUriConst.MYSUBSCRIBE)
public class MySubscribeActivity extends JMEBaseActivity {

    private ActivityMySubscribeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_subscribe;
    }

    @Override
    protected void initView() {
        super.initView();
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

        mBinding = (ActivityMySubscribeBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

    }
}
