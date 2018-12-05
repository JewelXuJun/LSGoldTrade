package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityHistoryHoldPositionBinding;

@Route(path = Constants.ARouterUriConst.HISTORYHOLDPOSITION)
public class HistoryHoldPositionActivity extends JMEBaseActivity {

    private ActivityHistoryHoldPositionBinding mBinding;

    private HistoryHoldPositionAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityHistoryHoldPositionBinding) mBindingUtil;

        initToolbar(R.string.trade_history_hold_position, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HistoryHoldPositionAdapter(R.layout.item_history_hold_position, null);

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

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickSelectDate(int position) {

        }

    }
}
