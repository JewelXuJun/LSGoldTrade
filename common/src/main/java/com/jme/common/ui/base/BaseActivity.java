package com.jme.common.ui.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jme.common.network.API;
import com.jme.common.network.AsynCommon;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.network.OnResultListener;
import com.jme.common.ui.view.LoadingDialog;
import com.jme.common.util.AppManager;

import java.util.HashMap;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnResultListener {

    protected Toast mToast;
    protected Context mContext;
    protected LoadingDialog mLoadingDialog;
    protected ToolbarHelper mToolbarHelper;
    protected ViewDataBinding mBindingUtil;

    protected boolean mUseBinding = true;

    private static final int MSG_ERROR_MSG_REPET = 1000;

    private String mPrevErrorCode = "";

    private Handler mErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_ERROR_MSG_REPET:
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPET);
                    mPrevErrorCode = "";

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        AppManager.getAppManager().addActivity(this);

        if (mUseBinding)
            mBindingUtil = DataBindingUtil.setContentView(this, getContentViewId());
        else if (getContentViewId() != 0 && !mUseBinding)
            setContentView(getContentViewId());

        initView();
        initData(savedInstanceState);
        initListener();
    }

    protected abstract int getContentViewId();

    protected void initView() {

    }

    protected void initData(Bundle savedInstanceState) {

    }

    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
    }

    public void initToolbar(View view, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(view);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(title);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(title, resId);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String[] titils, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);
        setBackNavigation(hasBack);
        mToolbarHelper.initToolbar(titils);
    }

    public void initToolbar(int resId, boolean hasBack) {
        initToolbar(getString(resId), hasBack);
    }

    public void initToolbar(int resId, boolean hasBack, @ColorInt int colorResID) {
        initToolbar(getString(resId), hasBack, colorResID);
    }

    public void setTitle(int resId) {
        if (resId != 0)
            setTitle(getString(resId));
    }

    public void setTitle(String title) {
        if (mToolbarHelper != null)
            mToolbarHelper.setTitle(title);
    }

    public void setBackNavigation(boolean hasBack) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigation(hasBack, (view) -> onBackPressed());
    }

    public void setBackNavigation(boolean hasBack, @DrawableRes int drawableResId) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigation(hasBack, drawableResId, (view) -> onBackPressed());
    }

    public void setBackNavigation(@DrawableRes int drawableResId, View.OnClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigationIcon(drawableResId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, @StyleRes int _resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, _resId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, listener);
    }

    protected void showShortToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(mContext, getResources().getString(resId), Toast.LENGTH_SHORT);
                else
                    mToast.setText(resId);

                mToast.show();
            }
        });
    }

    protected void showShortToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null)
                        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                    else
                        mToast.setText(text);

                    mToast.show();
                }
            });
        }
    }

    protected void showLongToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(mContext, getResources().getString(resId), Toast.LENGTH_LONG);
                else
                    mToast.setText(resId);

                mToast.show();
            }
        });
    }

    protected void showLongToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null)
                        mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
                    else
                        mToast.setText(text);

                    mToast.show();
                }
            });
        }
    }

    protected void startAnimActivity(Class<?> cls) {
        this.startAnimActivity(cls, null);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
    }

    protected void startAnimActivityForResult(Class<?> cls, int requestCode) {
        this.startAnimActivityForResult(cls, null, requestCode);
    }

    protected void startAnimActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    protected void showLoadingDialog(String text) {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(mContext);

        mLoadingDialog.setLoadingText(text);

        if (!isFinishing() && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog, boolean showErrorMsgOneTime, boolean showErrorMsg) {
        if (showprogressDialog)
            showLoadingDialog("");

        AsynCommon task = AsynCommon.SendRequest(api, params, showErrorMsgOneTime, showErrorMsg, this, mContext);

        return task;
    }

    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog, boolean showErrorMsgOneTime) {
        return sendRequest(api, params, showprogressDialog, showErrorMsgOneTime, true);
    }

    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog) {
        return sendRequest(api, params, showprogressDialog, false, true);
    }

    @Override
    public void OnResult(DTRequest request, Head head, Object response) {
        dismissLoadingDialog();
        handleErrorInfo(request, head);
        DataReturn(request, head.getCode(), response);
        DataReturn(request, head, response);
    }

    protected void DataReturn(DTRequest request, String msgCode, Object response) {

    }

    protected void DataReturn(DTRequest request, Head head, Object response) {

    }

    private void handleErrorInfo(DTRequest request, Head head) {
        if (head.getCode() != null && !head.getCode().equals("0")) {
            if (!request.isShowErrorMsg()) {
                return;
            }

            if (request.isSilent()) {
                if (!head.getCode().equals(mPrevErrorCode)) {
                    mPrevErrorCode = head.getCode();
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPET);
                    mErrorHandler.sendEmptyMessageDelayed(MSG_ERROR_MSG_REPET, 120 * 1000);

                    showShortToast(head.getMsg());
                }
            } else {
                showShortToast(head.getMsg());
            }
        }
    }

}
