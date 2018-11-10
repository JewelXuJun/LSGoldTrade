package com.jme.lsgoldtrade.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseActivity;
import com.jme.lsgoldtrade.domain.User;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseActivity<T> extends BaseActivity {

    protected boolean isFinishing = false;

    protected T mBinding;

    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
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

        initBinding();

        mUser = User.getInstance();
    }

    @Override
    protected void initListener() {
        super.initListener();
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
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

}
