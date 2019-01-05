package com.jme.lsgoldtrade.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityNewsCenterBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

@Route(path = Constants.ARouterUriConst.NEWSCENTERACTIVITY)
public class NewsCenterActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityNewsCenterBinding mBinding;

    private NewsAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_center;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.news_title, true);

        mBinding = (ActivityNewsCenterBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new NewsAdapter(R.layout.item_news, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
