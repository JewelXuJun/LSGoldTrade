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
import android.support.annotation.MenuRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jme.common.network.API;
import com.jme.common.network.AsynCommon;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.network.OnResultListener;
import com.jme.common.ui.view.LoadingDialog;

import java.util.HashMap;

/**
 * Fragment基类
 * Created by XuJun on 2018/11/7.
 */
public abstract class BaseFragment<T> extends Fragment implements View.OnTouchListener, OnResultListener {

    protected Toast mToast;
    protected LoadingDialog mLoadingDialog;
    protected Context mContext;
    protected AppCompatActivity mActivity;
    protected ToolbarHelper mToolbarHelper;
    protected ViewDataBinding mBindingUtil;

    protected boolean mHide = true;
    protected boolean mUseBinding = true;
    protected boolean mVisible = false;

    protected T mBinding;

    private static final int MSG_ERROR_MSG_REPEAT = 1000;

    private String mPrevErrorCode = "";

    private Handler mErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_ERROR_MSG_REPEAT:
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPEAT);
                    mPrevErrorCode = "";

                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        mVisible = isVisibleToUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        int id = getContentViewId();

        View view;

        if (mUseBinding) {
            mBindingUtil = DataBindingUtil.inflate(inflater, id, container, false);

            if (mBindingUtil == null)
                view = inflater.inflate(id, container, false);
            else
                view = mBindingUtil.getRoot();
        } else {
            view = inflater.inflate(id, container, false);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData(savedInstanceState);
        initListener();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        mHide = hidden;
    }

    public void initToolbar(View view, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());
        mToolbarHelper.initToolbar(view);

        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());
        mToolbarHelper.initToolbar(title);

        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());
        mToolbarHelper.initToolbar(title, resId);

        setBackNavigation(hasBack);
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
            mToolbarHelper.setBackNavigation(hasBack, (view) -> mActivity.onBackPressed());
    }

    public void setRightNavigation(String str, @DrawableRes int resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, @StyleRes int _resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, _resId, listener);
    }

    public void setRightMultiNavigation(@MenuRes int resId, final Toolbar.OnMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setMenu(resId, listener);
    }

    public void setRightMultiNavigation(@MenuRes int resId, @StyleRes int _resId, final Toolbar.OnMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setMenu(resId, _resId, listener);
    }

    public void clearRightNavigation() {
        if (mToolbarHelper != null)
            mToolbarHelper.clearMenu();
    }

    protected void showShortToast(final int resId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
                else
                    mToast.setText(resId);

                mToast.show();
            }
        });
    }

    protected void showShortToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null)
                        mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
                    else
                        mToast.setText(text);

                    mToast.show();
                }
            });
        }
    }

    protected void showLongToast(final int resId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
                else
                    mToast.setText(resId);

                mToast.show();
            }
        });
    }

    protected void showLongToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null)
                        mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
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
        intent.setClass(mActivity, cls);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
    }

    protected void startAnimActivityForResult(Class<?> cls, int requestCode) {
        this.startAnimActivityForResult(cls, null, requestCode);
    }

    protected void startAnimActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    protected void showLoadingDialog(String text) {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(mActivity);

        mLoadingDialog.setLoadingText(text);

        if (!mActivity.isFinishing() && !mLoadingDialog.isShowing())
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
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPEAT);
                    mErrorHandler.sendEmptyMessageDelayed(MSG_ERROR_MSG_REPEAT, 120 * 1000);

                    showShortToast(head.getMsg());
                }
            } else {
                showShortToast(head.getMsg());
            }
        }
    }

    protected abstract int getContentViewId();

    protected void initView() {

    }

    protected void initData(Bundle savedInstanceState) {

    }

    protected void initListener() {

    }

    protected View findViewById(int resId) {
        return getView().findViewById(resId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 拦截触摸事件，防止传递到下一层的View
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
