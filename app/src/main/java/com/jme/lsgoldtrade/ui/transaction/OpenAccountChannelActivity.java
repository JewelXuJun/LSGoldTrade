package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOpenAccountChannelBinding;

@Route(path = Constants.ARouterUriConst.OPENACCOUNTCHANNEL)
public class OpenAccountChannelActivity extends JMEBaseActivity {

    ///getHFBankOpenAccountUrl?customerName=%@&idCode=%@。获取恒丰开户h5链接的

    private ActivityOpenAccountChannelBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_open_account_channel;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_select_open_account_channel, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityOpenAccountChannelBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickOpenAccountHF() {
            ARouter.getInstance().build(Constants.ARouterUriConst.BINDACCOUNTHF).navigation();
        }

        public void onClickOpenAccountICBC() {

        }

        public void onClickBindAccountICBC() {

        }

        public void onClickCourseHF() {

        }

        public void onClickCourseICBC() {

        }

    }
}
