package com.jme.lsgoldtrade.base;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseActivity;
import com.jme.common.util.DialogHelp;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.ui.login.AccountLoginActivity;
import com.jme.lsgoldtrade.ui.login.MobileLoginActivity;
import com.jme.lsgoldtrade.ui.splash.SplashActivity;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

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

        initBinding();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mUser = User.getInstance();
        mContract = Contract.getInstance();
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
                    mUser.logout();

                    SharedPreUtils.setString(this, SharedPreUtils.Token, "");
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);

                    showLoginDialog(message.getObject2().toString());

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

        if (head.getCode().equals("-2000")) {
            mUser.logout();

            SharedPreUtils.setString(this, SharedPreUtils.Token, "");
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);

            if (!currentClass().equals(SplashActivity.class.getName()) && !currentClass().equals(AccountLoginActivity.class.getName())
                    && !currentClass().equals(MobileLoginActivity.class.getName()))
                showLoginDialog(head.getMsg());
        } else {
            handleErrorInfo(request, head);
        }
    }

    protected void showLoginDialog(String msg) {
        if (isFinishing)
            return;

        if (!isForeground())
            return;

        if (null == mDialog) {
            mDialog = DialogHelp.getConfirmDialog(this, getString(R.string.text_tips), msg,
                    getString(R.string.text_login),
                    (dialog, which) -> {
                        dialog.dismiss();

                        returntoLogin();

                        mDialog = null;
                    },
                    (dialog, which) -> {
                        dialog.dismiss();

                        returnToHomePage();

                        mDialog = null;
                    })
                    .setCancelable(false)
                    .show();
        } else {
            if (!mDialog.isShowing() && !isFinishing)
                mDialog.show();
        }
    }

    protected void dismissLoginDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();

            mDialog = null;
        }
    }

    private void returntoLogin() {
        gotoLogin();
    }

    private void returnToHomePage() {
        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);

        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
    }

    protected void gotoLogin() {
        String loginType = SharedPreUtils.getString(this, SharedPreUtils.Login_Type);

        if (TextUtils.isEmpty(loginType)) {
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        } else {
            if (loginType.equals("Account"))
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (loginType.equals("Mobile"))
                ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
        }
    }

    public boolean isForeground() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);

        if (list != null && list.size() > 0) {
            ComponentName componentName = list.get(0).topActivity;

            if (currentClass().equals(componentName.getClassName()))
                return true;
        }

        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
