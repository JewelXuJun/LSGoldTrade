package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCurrentEntrustBinding;

@Route(path = Constants.ARouterUriConst.CURRENTENTRUST)
public class CurrentEntrustActivity extends JMEBaseActivity {

    private ActivityCurrentEntrustBinding mBinding;

    private EntrustAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentEntrustBinding) mBindingUtil;

        initToolbar(R.string.trade_current_entrust, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(R.layout.item_entrust, null);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
