package com.jme.lsgoldtrade.ui.trade;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.datai.common.charts.fchart.FData;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.NetWorkUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentDeclarationFormBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class DeclarationFormFragment extends JMEBaseFragment {

    private FragmentDeclarationFormBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;
    private String[] mContractNames;

    private TabViewPagerAdapter mAdapter;
    private AlertDialog dialog;
    private ContractInfoVo mContractInfoVo;
    private TenSpeedVo mTenSpeedVo;

    private boolean bVisibleToUser = false;
    private boolean bFlag = true;
    private int mSelectItem = 0;
    private int mPriceType = TYPE_RIVALPRICE;

    private static int TYPE_RIVALPRICE = 0;
    private static int TYPE_QUEUINGPRICE = 1;
    private static int TYPE_LASTPRICE = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_TRADE_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

                    getTenSpeedQuotes();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_UPDATE_DATA, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_declaration_form;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentDeclarationFormBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        initInfoTabs();
        initContractNameValue();
        setPriceTypeLayout();
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
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        if (null != mHandler && !bVisibleToUser)
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

        super.onHiddenChanged(hidden);

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            setContractNameData();
            getTenSpeedQuotes();
        } else {
            if (null != mHandler)
                mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);
        }

        if (null != mBinding && null != mBinding.tabViewpager && null != mAdapter)
            mAdapter.getItem(mBinding.tabViewpager.getCurrentItem()).setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser) {
            bFlag = true;

            setContractNameData();
            getTenSpeedQuotes();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);
    }

    private void initInfoTabs() {
        mTabTitles = new String[4];
        mTabTitles[0] = mContext.getResources().getString(R.string.trade_hold_position);
        mTabTitles[1] = mContext.getResources().getString(R.string.trade_entrust);
        mTabTitles[2] = mContext.getResources().getString(R.string.trade_deal);
        mTabTitles[3] = mContext.getResources().getString(R.string.trade_cancel_order);

        mFragmentArrays = new Fragment[4];
        mFragmentArrays[0] = new ItemHoldPositionFragment();
        mFragmentArrays[1] = new ItemEntrustFragment();
        mFragmentArrays[2] = new ItemDealFragment();
        mFragmentArrays[3] = new ItemCancelOrderFragment();

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(4);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
        mBinding.tablayout.post(() -> setIndicator(mBinding.tablayout, 30, 30));
    }

    private void initContractNameValue() {
        if (null != mContract) {
            String listStr = mContract.getContractNameListStr();

            if (!TextUtils.isEmpty(listStr))
                mContractNames = listStr.split(",");
        }
    }

    private void setContractNameData() {
        if (null == mBinding)
            return;

        if (!mBinding.tvContractName.getText().toString().equals(AppConfig.Select_ContractName)) {
            mBinding.tvContractName.setText(AppConfig.Select_ContractName);

            if (null != mContract) {
                mSelectItem = mContract.getContractNamePosition(mBinding.tvContractName.getText().toString());
                mContractInfoVo = mContract.getContractInfoFromName(mBinding.tvContractName.getText().toString());
            }
        }
    }

    private void showContractNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(mContractNames, mSelectItem, (dialogInterface, position) -> {
            mSelectItem = position;

            String contractName = mContractNames[mSelectItem];

            mBinding.tvContractName.setText(contractName);
            mContractInfoVo = mContract.getContractInfoFromName(contractName);

            if (null == mContractInfoVo)
                return;

            AppConfig.Select_ContractName = contractName;
            AppConfig.Select_ContractId = mContractInfoVo.getContractId();

            mHandler.removeMessages(Constants.Msg.MSG_TRADE_UPDATE_DATA);

            getTenSpeedQuotes();

            mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_down));

            dialog.dismiss();
        });

        dialog = builder.create();
        dialog.show();
    }

    private void setPriceTypeLayout() {
        mBinding.btnRivalPrice.setBackground(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnRivalPrice.setTextColor(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnQueuingPrice.setBackground(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnQueuingPrice.setTextColor(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnLastPrice.setBackground(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnLastPrice.setTextColor(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));

        if (mPriceType == TYPE_RIVALPRICE)
            mBinding.etPrice.setHint(R.string.market_rival_price);
        else if (mPriceType == TYPE_QUEUINGPRICE)
            mBinding.etPrice.setHint(R.string.market_queuing_price);
        else if (mPriceType == TYPE_LASTPRICE)
            mBinding.etPrice.setHint(R.string.market_last_price);
        else
            mBinding.etPrice.setHint(R.string.market_price_hint);

        setPriceData();
    }

    private void setPriceData() {
        if (!TextUtils.isEmpty(mBinding.etPrice.getText().toString()))
            return;

        if (null == mTenSpeedVo) {
            mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
            mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
        } else {
            List<String[]> askLists = mTenSpeedVo.getAskLists();
            List<String[]> bidLists = mTenSpeedVo.getBidLists();

            if (mPriceType == TYPE_RIVALPRICE) {
                mBinding.tvPriceBuyMore.setText(askLists.get(9)[1]);
                mBinding.tvPriceSaleEmpty.setText(bidLists.get(0)[1]);
            } else if (mPriceType == TYPE_QUEUINGPRICE) {
                mBinding.tvPriceBuyMore.setText(bidLists.get(0)[1]);
                mBinding.tvPriceSaleEmpty.setText(askLists.get(9)[1]);
            } else if (mPriceType == TYPE_LASTPRICE) {
                mBinding.tvPriceBuyMore.setText(mTenSpeedVo.getLatestPrice());
                mBinding.tvPriceSaleEmpty.setText(mTenSpeedVo.getLatestPrice());
            } else {
                mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
                mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
            }
        }
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getTenSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", AppConfig.Select_ContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {
                    List<TenSpeedVo> list;

                    try {
                        list = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    mTenSpeedVo = list.get(0);

                    if (null == mTenSpeedVo)
                        return;

                    if (!mBinding.tvContractName.getText().toString().equals(mTenSpeedVo.getName()))
                        return;

                    String lastSettlePrice = mTenSpeedVo.getLastSettlePrice();
                    String latestPrice = mTenSpeedVo.getLatestPrice();

                    if (TextUtils.isEmpty(lastSettlePrice))
                        return;

                    mBinding.tvPrice.setText(latestPrice);
                    mBinding.tvPrice.setTextColor(ContextCompat.getColor(mContext,
                            MarketUtil.getMarketStateColor(new BigDecimal(latestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
                    mBinding.tvLimitDownPrice.setText(mTenSpeedVo.getLowerLimitPrice());
                    mBinding.tvLimitUpPrice.setText(mTenSpeedVo.getHighLimitPrice());
                    mBinding.tvAmount.setText(MarketUtil.getVolumeValue(String.valueOf(mTenSpeedVo.getTurnover()), false));
                    mBinding.fchartSale.setData(mTenSpeedVo.getAskLists(), FData.TYPE_SELL, lastSettlePrice);
                    mBinding.fchartBuy.setData(mTenSpeedVo.getBidLists(), FData.TYPE_BUY, lastSettlePrice);

                    setPriceData();
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_UPDATE_DATA, getTimeInterval());
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSelectContract() {
            if (null != mContractNames && mContractNames.length > 0) {
                mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_up));

                showContractNameDialog();
            }
        }

        public void onClickLimitDownPrice() {

        }

        public void onClickLimitUpPrice() {

        }

        public void onClickPriceMinus() {

        }

        public void onClickPriceAdd() {

        }

        public void onClickRivalPrice() {
            if (mPriceType == TYPE_RIVALPRICE)
                return;

            mPriceType = TYPE_RIVALPRICE;

            setPriceTypeLayout();
        }

        public void onClickQueuingPrice() {
            if (mPriceType == TYPE_QUEUINGPRICE)
                return;

            mPriceType = TYPE_QUEUINGPRICE;

            setPriceTypeLayout();
        }

        public void onClickLastPrice() {
            if (mPriceType == TYPE_LASTPRICE)
                return;

            mPriceType = TYPE_LASTPRICE;

            setPriceTypeLayout();
        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onClickBuyMore() {

        }

        public void onClickSaleEmpty() {

        }

        public void onClickEveningUp() {

        }

    }

    final class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

}
