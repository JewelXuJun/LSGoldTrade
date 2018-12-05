package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMessageCenterBinding;

@Route(path = Constants.ARouterUriConst.MESSAGECENTER)
public class MessageCenterActivity extends JMEBaseActivity {

    private ActivityMessageCenterBinding mBinding;

    private MessageDetailAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMessageCenterBinding) mBindingUtil;

        initToolbar(R.string.personal_message_center, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new MessageDetailAdapter(R.layout.item_message_detail, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
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
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
