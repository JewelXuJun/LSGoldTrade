package com.jme.lsgoldtrade.base;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

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
import com.jme.lsgoldtrade.view.ArrearsDialog;
import com.jme.lsgoldtrade.view.ArrearsMinimalism;
import com.jme.lsgoldtrade.view.ArrearsPayDialog;
import com.jme.lsgoldtrade.view.IncrementAlertDialog;
import com.jme.lsgoldtrade.view.LaterStageIncrenmentDialog;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import rx.Subscription;

public abstract class JMEBaseActivity<T> extends BaseActivity {

    protected boolean isFinishing = false;

    protected T mBinding;
    protected User mUser;
    protected Contract mContract;

    protected static Dialog mDialog;

    private ArrearsDialog mArrearsDialog;
    private Subscription mRxbus;

    private LaterStageIncrenmentDialog mLaterStageIncrenmentDialog;

    private IncrementAlertDialog mIncrementAlertOpening;
    private IncrementAlertDialog mIncrementAlertClose;
    private ArrearsPayDialog mArrearsPayDialog;
    private ArrearsMinimalism mArrearsMinimalism;

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
                case Constants.RxBusConst.RXBUS_INCREMENT_ARREARS:
                    showArrearsDialog();

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
        }
//        else if (head.getCode().equals("-2011")) {
//            showArrearsDialog();
//        }
        else if(head.getCode().equals("-2012")||head.getCode().equals("-2018")||head.getCode().equals("-2017")){
            //您当前尚未开通增值服务,请开通后再使用该功能  继续开通  取消
            showLaterDialog();
        }else if(head.getCode().equals("-2013")){
            Log.i("testXin","接收2222");
            showIncrementOpeningDialog("您申请的开通增值服务尚未生效，请等待生效后使用。生效周期为申请之时起的1个交易日内。");
        }else if(head.getCode().equals("-2014")){
            showIncrementCloseDialog("您申请的关闭增值服务还在审核中，暂时无法使用该功能。");
        }else if(head.getCode().equals("-2015")){
            showArrearsPayDialog();
        }else if(head.getCode().equals("-2016")){
            showArrearsMinimalismDialog();
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

    private void showArrearsDialog() {
        if (isFinishing)
            return;

        if (null == mArrearsDialog)
            mArrearsDialog = new ArrearsDialog(mContext);

        if (!mArrearsDialog.isShowing()) {
            mArrearsDialog.show();

            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams params = mArrearsDialog.getWindow().getAttributes();
            params.width = (int) (display.getWidth() * 0.75);
            mArrearsDialog.getWindow().setAttributes(params);
        }
    }

    private void showLaterDialog(){
        if (isFinishing)
            return;

        if (null == mLaterStageIncrenmentDialog)
            mLaterStageIncrenmentDialog = new LaterStageIncrenmentDialog(mContext);

        if (!mLaterStageIncrenmentDialog.isShowing()) {
            mLaterStageIncrenmentDialog.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mLaterStageIncrenmentDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mLaterStageIncrenmentDialog.getWindow().setAttributes(lp);

        }
    }

    private void showIncrementOpeningDialog(String content){
        if (isFinishing)
            return;

        if (null == mIncrementAlertOpening)
            mIncrementAlertOpening = new IncrementAlertDialog(mContext,content);

        if (!mIncrementAlertOpening.isShowing()) {
            mIncrementAlertOpening.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mIncrementAlertOpening.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mIncrementAlertOpening.getWindow().setAttributes(lp);

        }
    }

    private void showIncrementCloseDialog(String content){
        if (isFinishing)
            return;

        if (null == mIncrementAlertClose)
            mIncrementAlertClose = new IncrementAlertDialog(mContext,content);

        if (!mIncrementAlertClose.isShowing()) {
            mIncrementAlertClose.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mIncrementAlertClose.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mIncrementAlertClose.getWindow().setAttributes(lp);

        }
    }

    private void showArrearsPayDialog(){
        if (isFinishing)
            return;

        if (null == mArrearsPayDialog)
            mArrearsPayDialog = new ArrearsPayDialog(mContext);

        if (!mArrearsPayDialog.isShowing()) {
            mArrearsPayDialog.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mArrearsPayDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mArrearsPayDialog.getWindow().setAttributes(lp);

        }
    }

    private void showArrearsMinimalismDialog(){
        if (isFinishing)
            return;

        if (null == mArrearsMinimalism)
            mArrearsMinimalism = new ArrearsMinimalism(mContext);

        if (!mArrearsMinimalism.isShowing()) {
            mArrearsMinimalism.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mArrearsMinimalism.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mArrearsMinimalism.getWindow().setAttributes(lp);

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
