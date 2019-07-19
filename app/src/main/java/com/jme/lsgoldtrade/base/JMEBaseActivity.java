package com.jme.lsgoldtrade.base;

import android.app.AlertDialog;
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
import com.umeng.socialize.UMShareAPI;

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
    private AlertDialog.Builder mNeedLoginDialog;

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
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCEL_MAIN, null);
                    else if (!currentClass().equals(MarketDetailActivity.class.getName()) && !currentClass().equals(MarketDetailLandscapeActivity.class.getName()))
                        showLoginDialog();

                    break;
            }
        });
    }

    protected String currentClass() {
        return getClass().getName();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        if (head.getCode().equals("-2000"))
            showLoginDialog();
    }

    protected void showLoginDialog() {
        if (isFinishing)
            return;

        if (null == mDialog) {
            mDialog = DialogHelp.getConfirmDialog(this, getString(R.string.text_tips), getString(R.string.text_message_notlogin),
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
        } else {
            if (!mDialog.isShowing())
                mDialog.show();
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

    protected void showNeedLoginDialog() {
        if (null == mNeedLoginDialog) {
            mNeedLoginDialog = new AlertDialog.Builder(this);
            mNeedLoginDialog.setTitle(mContext.getResources().getString(R.string.text_tips));
            mNeedLoginDialog.setMessage(mContext.getResources().getString(R.string.login_message));
            mNeedLoginDialog.setPositiveButton(mContext.getResources().getString(R.string.text_login), (dialog, which) -> ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation());
            mNeedLoginDialog.setNegativeButton(mContext.getResources().getString(R.string.text_cancel), null);
            mNeedLoginDialog.show();
        } else {
            mNeedLoginDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
