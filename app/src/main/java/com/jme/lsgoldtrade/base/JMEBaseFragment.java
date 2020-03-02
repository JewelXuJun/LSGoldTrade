package com.jme.lsgoldtrade.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseFragment;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.view.ArrearsMinimalism;
import com.jme.lsgoldtrade.view.ArrearsPayDialog;
import com.jme.lsgoldtrade.view.IncrementAlertDialog;
import com.jme.lsgoldtrade.view.LaterStageIncrenmentDialog;

import java.util.List;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseFragment<T> extends BaseFragment {

    protected boolean isFinishing = false;

    protected T mBinding;
    protected User mUser;
    protected Contract mContract;
    private LaterStageIncrenmentDialog mLaterStageIncrenmentDialog;

    private IncrementAlertDialog mIncrementAlertOpening;
    private IncrementAlertDialog mIncrementAlertClose;
    private ArrearsPayDialog mArrearsPayDialog;
    private ArrearsMinimalism mArrearsMinimalism;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFinishing = mActivity.isFinishing();
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
    }

    public void initBinding() {
        mBinding = (T) mBindingUtil;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isHidden())
            return;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden && !isHidden())
            onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    protected void gotoLogin() {
        String loginType = SharedPreUtils.getString(mContext, SharedPreUtils.Login_Type);

        if (TextUtils.isEmpty(loginType)) {
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        } else {
            if (loginType.equals("Account"))
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (loginType.equals("Mobile"))
                ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        isFinishing = mActivity.isFinishing();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        if (head.getCode().equals("-2000")) {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_SYNTIME, head.getMsg());
        }
//        else if (head.getCode().equals("-2011"))
//            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_INCREMENT_ARREARS, null);
        else if (head.getCode().equals("-2012")||head.getCode().equals("-2018")||head.getCode().equals("-2017")) {
            showLaterDialog();
        }else if (head.getCode().equals("-2013")) {
            showIncrementOpeningDialog("您申请的开通增值服务尚未生效，请等待生效后使用。生效周期为申请之时起的1个交易日内。");
        }else if (head.getCode().equals("-2014")) {
            showIncrementCloseDialog("您申请的关闭增值服务还在审核中，暂时无法使用该功能。");
        }else if (head.getCode().equals("-2015")) {
            showArrearsPayDialog();
        }else if (head.getCode().equals("-2016")) {
            showArrearsMinimalismDialog();
        }else {
            handleErrorInfo(request, head);
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
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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

            if (mActivity.getClass().getName().equals(componentName.getClassName()))
                return true;
        }

        return false;

    }
}
