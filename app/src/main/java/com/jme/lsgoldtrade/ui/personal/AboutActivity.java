package com.jme.lsgoldtrade.ui.personal;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAboutBinding;

/**
 * 关于
 */
@Route(path = Constants.ARouterUriConst.ABOUT)
public class AboutActivity extends JMEBaseActivity {

    private ActivityAboutBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityAboutBinding) mBindingUtil;

        initToolbar(R.string.personal_about, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        try {
            mBinding.tvVersion.setText(String.format(getString(R.string.personal_aboutus_version), getVersionName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private String getVersionName() throws Exception {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;

        return version;
    }

    public class ClickHandlers {

        public void onClickFeedBack() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.FEEDBACK)
                        .navigation();
        }

        public void onClickDisclaimer() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.personal_about_disclaimer))
                    .withString("url", Constants.HttpConst.URL_DISCLAIMER)
                    .navigation();
        }

        public void onClickPrivacyPolicy() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.personal_about_privacy_policy))
                    .withString("url", Constants.HttpConst.URL_PRIVACY_POLICY)
                    .navigation();
        }
    }
}
