package com.jme.lsgoldtrade.ui.trade;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityHistoryDealBinding;
import com.jme.lsgoldtrade.databinding.ActivityHistoryEntrustBinding;
import com.jme.lsgoldtrade.domain.DealHistoryPageVo;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.HISTORYDEAL)
public class HistoryDealActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityHistoryDealBinding mBinding;

    private DealHistoryAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private String mSearchKey = "";

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_deal;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityHistoryDealBinding) mBindingUtil;

        initToolbar(R.string.trade_history_deal, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new DealHistoryAdapter(R.layout.item_deal_history, null);
        mAdapter.clearDate();

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
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

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDealHistory(true);
    }

    private void initDate() {
        getTodayCalendar();

        mStartTime = getStartTime(mYear, mMonth, mDayOfMonth);
        mEndTime = getEndTime(mYear, mMonth, mDayOfMonth);

        mBinding.tvStartTime.setText(DateUtil.dateToString(mStartTime));
        mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));
    }

    private void getTodayCalendar() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private long getStartTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    private long getEndTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    private void setStartTime(int year, int month, int dayOfMonth) {
        long startTime = getStartTime(year, month, dayOfMonth);

        if (startTime <= mEndTime) {
            mStartTime = startTime;

            mBinding.tvStartTime.setText(DateUtil.dateToString(mStartTime));
        } else {
            showShortToast(R.string.trade_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;

            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));
        } else {
            showShortToast(R.string.trade_end_time_error);
        }
    }

    private void initDealHistory(boolean enablle) {
        mCurrentPage = 1;
        mSearchKey = "";

        mAdapter.clearDate();

        dealhispage(enablle);
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void setEmptyData() {
        mBinding.swipeRefreshLayout.finishRefresh(false);
        mAdapter.loadMoreFail();
    }

    private void dealhispage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("beginDate", DateUtil.dateToStringWithNone(mStartTime));
        params.put("endDate", DateUtil.dateToStringWithNone(mEndTime));
        params.put("searchKey", mSearchKey);

        sendRequest(TradeService.getInstance().dealhispage, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DealHisPage":
                if (head.isSuccess()) {
                    DealHistoryPageVo dealHistoryPageVo;

                    try {
                        dealHistoryPageVo = (DealHistoryPageVo) response;
                    } catch (Exception e) {
                        dealHistoryPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == dealHistoryPageVo) {
                        setEmptyData();
                    } else {
                        mSearchKey = dealHistoryPageVo.getSeachKey();
                        List<DealHistoryPageVo.DealHistoryBean> dealHistoryBeanList = dealHistoryPageVo.getList();

                        if (TextUtils.isEmpty(mSearchKey)) {
                            if (mCurrentPage == 1) {
                                if (null == dealHistoryBeanList || 0 == dealHistoryBeanList.size()) {
                                    mAdapter.setEmptyView(getEmptyView());
                                    mBinding.swipeRefreshLayout.finishRefresh(true);
                                } else {
                                    mAdapter.setNewData(dealHistoryBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mAdapter.addData(dealHistoryBeanList);
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                            if (mCurrentPage == 1)
                                mAdapter.setNewData(dealHistoryBeanList);
                            else
                                mAdapter.addData(dealHistoryBeanList);

                            mAdapter.loadMoreComplete();
                        }
                    }
                } else {
                    setEmptyData();
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (TextUtils.isEmpty(mSearchKey)) {
                mAdapter.loadMoreEnd();
            } else {
                mCurrentPage++;

                dealhispage(true);
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initDealHistory(false);
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(HistoryDealActivity.this, (datePicker, year, month, dayOfMonth) -> {
                if (timeType == TIME_START)
                    setStartTime(year, month, dayOfMonth);
                else if (timeType == TIME_END)
                    setEndTime(year, month, dayOfMonth);
            }, mYear, mMonth, mDayOfMonth);

            if (android.os.Build.VERSION.SDK_INT >= 11)
                mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            mDatePickerDialog.show();
        }

    }
}
