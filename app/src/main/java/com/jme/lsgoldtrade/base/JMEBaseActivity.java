package com.jme.lsgoldtrade.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseActivity;
import com.jme.common.util.DialogHelp;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.ui.main.MainActivity;
import com.jme.lsgoldtrade.ui.market.MarketDetailActivity;
import com.jme.lsgoldtrade.ui.market.MarketDetailLandscapeActivity;

import java.lang.reflect.Field;

import rx.Subscription;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseActivity<T> extends BaseActivity {

    protected boolean isFinishing = false;

    protected T mBinding;

    protected User mUser;
    protected Contract mContract;
    private Subscription mRxbus;

    protected static Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);

            return;
        }

        isFinishing = isFinishing();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mUser = User.getInstance();
        mContract = Contract.getInstance();

        initBinding();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    protected void initBinding() {
        mBinding = (T) mBindingUtil;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isFinishing = isFinishing();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_SYNTIME:
                    if (currentClass().equals(MainActivity.class.getName()))
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCEL_MAIN);
                    else if (!currentClass().equals(MarketDetailActivity.class.getName()) && !currentClass().equals(MarketDetailLandscapeActivity.class.getName())
                            && (null == mDialog || (mDialog != null && !mDialog.isShowing())))
                        showLoginDialog();

                    break;
            }
        });
    }

    protected String currentClass() {
        return this.getClass().getName();
    }


    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    protected void showLoginDialog() {
        if (!isFinishing) {
            mDialog = DialogHelp.getConfirmDialog(mContext, getString(R.string.text_tips), getString(R.string.text_message_notlogin),
                    getString(R.string.text_login),
                    (dialog, which) -> {
                        dialog.dismiss();
                        returntoLogin();
                    },
                    (dialog, which) -> {
                        dialog.dismiss();
                        returnToHomePage();
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void returntoLogin() {
        ARouter.getInstance()
                .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                .navigation();
    }

    private void returnToHomePage() {
        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCEL, null);
        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

}
