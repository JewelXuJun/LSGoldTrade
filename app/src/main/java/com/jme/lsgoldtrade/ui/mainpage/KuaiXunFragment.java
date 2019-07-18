package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentKuaixunBinding;

public class KuaiXunFragment extends JMEBaseFragment {

    private FragmentKuaixunBinding mBinding;

    private String url = "https://www.jin10.com/example/jin10.com.html?fontSize=14px&theme=white";

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_kuaixun;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (FragmentKuaixunBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBinding.webview.loadUrl(url);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    public void initBinding() {
        super.initBinding();
    }
}
