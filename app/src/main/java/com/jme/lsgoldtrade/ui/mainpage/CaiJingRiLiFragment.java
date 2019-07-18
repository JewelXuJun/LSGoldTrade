package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentCaijingriliBinding;

public class CaiJingRiLiFragment extends JMEBaseFragment {

    private FragmentCaijingriliBinding mBinding;

    private String url = "https://rili-d.jin10.com/open.php?fontSize=14px&theme=primary";

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_caijingrili;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (FragmentCaijingriliBinding) mBindingUtil;
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
}
