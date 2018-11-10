package com.jme.lsgoldtrade.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.databinding.ActivityMainBinding;

import rx.Subscription;

/**
 * Created by XuJun on 2018/11/7.
 */
public class MainActivity extends JMEBaseActivity implements TabHost.OnTabChangeListener, View.OnTouchListener {

    private ActivityMainBinding mBinding;

    private long exitTime = 0;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMainBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.tabhost.setOnTabChangedListener(this);

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {

            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (view.equals(mBinding.tabhost.getTabWidget().getChildAt(MainTab.PERSONAL.getIdx()))) {

            } else if (view.equals(mBinding.tabhost.getTabWidget().getChildAt(MainTab.BALANCE.getIdx()))) {
                if (!mUser.isLogin()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.LOGIN)
                            .navigation();

                    return true;
                }
            }
        }*/

        return false;
    }

    @Override
    public void onTabChanged(String tabId) {
        int size = mBinding.tabhost.getTabWidget().getTabCount();

        for (int i = 0; i < size; i++) {
            View v = mBinding.tabhost.getTabWidget().getChildAt(i);

            v.setSelected(i == mBinding.tabhost.getCurrentTab() ? true : false);
        }

        supportInvalidateOptionsMenu();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar snackbar = Snackbar.make(mBinding.fragmentlayout, getString(R.string.text_exit_app), Snackbar.LENGTH_SHORT);
                snackbar.setAction(getString(R.string.text_cancel), v -> exitTime = 0)
                        .setActionTextColor(ContextCompat.getColor(this, R.color.white));
                View snakebarView = snackbar.getView();
                TextView textView = snakebarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.common_font_content_white));
                snackbar.show();

                exitTime = System.currentTimeMillis();
            } else {
                finish();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }.start();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
