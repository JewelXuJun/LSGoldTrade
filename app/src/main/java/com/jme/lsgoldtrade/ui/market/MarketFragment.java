package com.jme.lsgoldtrade.ui.market;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMarketBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.util.SortUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 行情
 */
public class MarketFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMarketBinding mBinding;

    private MarketAdapter mAdapter;
    private View mEmptyView;

    private List<FiveSpeedVo> mList;

    private boolean bHidden = false;
    private boolean bFlag = true;

    private SortUtil.ENUM_SORTS mCurrentSort = SortUtil.ENUM_SORTS.NONE;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_MARKET_UPDATE:
                    mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);

                    getMarket(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_MARKET_UPDATE, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

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

        mAdapter = new MarketAdapter(mContext, R.layout.item_market, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        setCurrentSortLayout();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FiveSpeedVo fiveSpeedVo = (FiveSpeedVo) adapter.getItem(position);

            if (null == fiveSpeedVo)
                return;

            String contractId = fiveSpeedVo.getContractId();

            if (TextUtils.isEmpty(contractId))
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MARKETDETAIL)
                    .withString("ContractId", contractId)
                    .navigation();
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        bHidden = hidden;

        if (!bHidden) {
            bFlag = true;

            getMarket(true);
        } else {
            mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!bHidden) {
            bFlag = true;

            getMarket(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void setCurrentSortLayout() {
        Drawable drawableContract = mCurrentSort == SortUtil.ENUM_SORTS.LETTER_ASC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_asc)
                : mCurrentSort == SortUtil.ENUM_SORTS.LETTER_DESC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_desc) : null;
        if (null != drawableContract)
            drawableContract.setBounds(0, 0, drawableContract.getMinimumWidth(), drawableContract.getMinimumHeight());
        mBinding.tvContract.setCompoundDrawables(null, null, drawableContract, null);

        Drawable drawableLastPrice = mCurrentSort == SortUtil.ENUM_SORTS.PRICE_ASC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_asc)
                : mCurrentSort == SortUtil.ENUM_SORTS.PRICE_DESC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_desc) : null;
        if (null != drawableLastPrice)
            drawableLastPrice.setBounds(0, 0, drawableLastPrice.getMinimumWidth(), drawableLastPrice.getMinimumHeight());
        mBinding.tvLastPrice.setCompoundDrawables(null, null, drawableLastPrice, null);

        Drawable drawableRange = mCurrentSort == SortUtil.ENUM_SORTS.RATE_ASC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_asc)
                : mCurrentSort == SortUtil.ENUM_SORTS.RATE_DESC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_desc) : null;
        if (null != drawableRange)
            drawableRange.setBounds(0, 0, drawableRange.getMinimumWidth(), drawableRange.getMinimumHeight());
        mBinding.tvRange.setCompoundDrawables(null, null, drawableRange, null);

        Drawable drawableVolume = mCurrentSort == SortUtil.ENUM_SORTS.VOLUME_ASC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_asc)
                : mCurrentSort == SortUtil.ENUM_SORTS.VOLUME_DESC ? ContextCompat.getDrawable(mContext, R.mipmap.ic_desc) : null;
        if (null != drawableVolume)
            drawableVolume.setBounds(0, 0, drawableVolume.getMinimumWidth(), drawableVolume.getMinimumHeight());
        mBinding.tvVolume.setCompoundDrawables(null, null, drawableVolume, null);
    }

    private void sortList(List<FiveSpeedVo> list) {
        if (null == list || 0 == list.size()) {
            mAdapter.setEmptyView(getEmptyView());
        } else {
            switch (mCurrentSort) {
                case NONE:

                    break;
                case LETTER_ASC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        Collator collator = Collator.getInstance();

                        String leftContract = leftValue.getContractId();
                        String rightContract = rightValue.getContractId();

                        return collator.getCollationKey(leftContract).compareTo(collator.getCollationKey(rightContract));
                    });

                    break;
                case LETTER_DESC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        Collator collator = Collator.getInstance();

                        String leftContract = leftValue.getContractId();
                        String rightContract = rightValue.getContractId();

                        return collator.getCollationKey(rightContract).compareTo(collator.getCollationKey(leftContract));
                    });

                    break;
                case PRICE_ASC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftLastPrice = leftValue.getLatestPrice();
                        long rightLastPrice = rightValue.getLatestPrice();

                        return new BigDecimal(leftLastPrice).compareTo(new BigDecimal(rightLastPrice));
                    });

                    break;
                case PRICE_DESC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftLastPrice = leftValue.getLatestPrice();
                        long rightLastPrice = rightValue.getLatestPrice();

                        return new BigDecimal(rightLastPrice).compareTo(new BigDecimal(leftLastPrice));
                    });

                    break;
                case RATE_ASC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftRate = leftValue.getUpDownRate();
                        long rightRate = rightValue.getUpDownRate();

                        return new BigDecimal(leftRate).compareTo(new BigDecimal(rightRate));
                    });

                    break;
                case RATE_DESC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftRate = leftValue.getUpDownRate();
                        long rightRate = rightValue.getUpDownRate();

                        return new BigDecimal(rightRate).compareTo(new BigDecimal(leftRate));
                    });

                    break;
                case VOLUME_ASC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftVolume = leftValue.getTurnVolume();
                        long rightVolume = rightValue.getTurnVolume();

                        return new BigDecimal(leftVolume).compareTo(new BigDecimal(rightVolume));
                    });

                    break;
                case VOLUME_DESC:
                    Collections.sort(list, (leftValue, rightValue) -> {
                        if (null == leftValue || null == rightValue)
                            return 0;

                        long leftVolume = leftValue.getTurnVolume();
                        long rightRate = rightValue.getTurnVolume();

                        return new BigDecimal(rightRate).compareTo(new BigDecimal(leftVolume));
                    });

                    break;
            }

            mActivity.runOnUiThread(() -> mAdapter.setNewData(list));
        }
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void getMarket(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    try {
                        mList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        mList = null;

                        e.getMessage();
                    }

                    sortList(mList);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_MARKET_UPDATE, getTimeInterval());
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);

        bFlag = true;

        getMarket(false);
    }

    public class ClickHandlers {

        public void onClickNews() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                        .navigation();
        }

        public void onClickSortContract() {
            if (mCurrentSort == SortUtil.ENUM_SORTS.LETTER_ASC)
                mCurrentSort = SortUtil.ENUM_SORTS.LETTER_DESC;
            else
                mCurrentSort = SortUtil.ENUM_SORTS.LETTER_ASC;

            setCurrentSortLayout();
            sortList(mList);
        }

        public void onClickSortLastPrice() {
            if (mCurrentSort == SortUtil.ENUM_SORTS.PRICE_ASC)
                mCurrentSort = SortUtil.ENUM_SORTS.PRICE_DESC;
            else
                mCurrentSort = SortUtil.ENUM_SORTS.PRICE_ASC;

            setCurrentSortLayout();
            sortList(mList);
        }

        public void onClickSortRange() {
            if (mCurrentSort == SortUtil.ENUM_SORTS.RATE_ASC)
                mCurrentSort = SortUtil.ENUM_SORTS.RATE_DESC;
            else
                mCurrentSort = SortUtil.ENUM_SORTS.RATE_ASC;

            setCurrentSortLayout();
            sortList(mList);
        }

        public void onClickSortVolume() {
            if (mCurrentSort == SortUtil.ENUM_SORTS.VOLUME_ASC)
                mCurrentSort = SortUtil.ENUM_SORTS.VOLUME_DESC;
            else
                mCurrentSort = SortUtil.ENUM_SORTS.VOLUME_ASC;

            setCurrentSortLayout();
            sortList(mList);
        }

    }
}
