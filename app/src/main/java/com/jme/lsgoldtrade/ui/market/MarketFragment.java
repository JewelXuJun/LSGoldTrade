package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMarketBinding;

public class MarketFragment extends JMEBaseFragment {

    private FragmentMarketBinding mBinding;

    private MarketAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMarketBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_toolbar_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new MarketAdapter(R.layout.item_market, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickNews() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MESSAGECENTER)
                    .navigation();
        }

        public void onClickSortContract() {

        }

        public void onClickSortLastPrice() {

        }

        public void onClickSortRange() {

        }

        public void onClickSortVolume() {

        }

    }
}
