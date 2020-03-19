package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityDeferredBinding;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 递延方向
 */
@Route(path = Constants.ARouterUriConst.DEFERRED)
public class DeferredActivity extends JMEBaseActivity {

    private ActivityDeferredBinding mBinding;

    private DeferredAdapter adapter;

    private List<String> list = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_deferred;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("递延方向", false);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        adapter = new DeferredAdapter(R.layout.item_deferred, list);
        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDateFromNet();
    }

    private void getDateFromNet() {
        sendRequest(ManagementService.getInstance().hasProfitLossRiskSign, new HashMap<>(), false);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityDeferredBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

    }
}
