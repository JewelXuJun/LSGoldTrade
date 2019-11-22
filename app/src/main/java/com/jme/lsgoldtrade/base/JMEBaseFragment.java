package com.jme.lsgoldtrade.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseFragment;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.config.User;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseFragment<T> extends BaseFragment {

    protected boolean isFinishing = false;

    protected T mBinding;
    protected User mUser;
    protected Contract mContract;

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

        if (head.getCode().equals("-2000"))
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_SYNTIME, head.getMsg());
        else if (head.getCode().equals("-2011")) {

        } else
            handleErrorInfo(request, head);
    }

}
