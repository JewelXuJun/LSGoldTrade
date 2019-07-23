package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRelevantInfoBinding;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;

import java.util.List;

/**
 * 相关资讯
 */
@Route(path = Constants.ARouterUriConst.RELEVANTINFO)
public class RelevantInfoActivity extends JMEBaseActivity {

    private ActivityRelevantInfoBinding mBinding;

    private String mInfoList;

    private RelevantInfoAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_relevant_info;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_info, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mInfoList = getIntent().getStringExtra("InfoList");

        if (TextUtils.isEmpty(mInfoList))
            return;

        mAdapter = new RelevantInfoAdapter(R.layout.item_relevant_info,
                new Gson().fromJson(mInfoList, new TypeToken<List<TradingBoxDetailsVo.RelevantInfoListVosBean>>() {
                }.getType()));

        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityRelevantInfoBinding) mBindingUtil;
    }

}
