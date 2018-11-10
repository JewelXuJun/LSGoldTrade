package com.jme.lsgoldtrade.base;

import android.content.Context;
import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseFragment;
import com.jme.lsgoldtrade.domain.User;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseFragment<T> extends BaseFragment {

    protected boolean isFinishing = false;

    protected T mBinding;

    protected User mUser;

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

    @Override
    public void onDetach() {
        super.onDetach();

        isFinishing = mActivity.isFinishing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
