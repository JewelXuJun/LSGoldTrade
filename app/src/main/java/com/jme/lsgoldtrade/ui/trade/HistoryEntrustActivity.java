package com.jme.lsgoldtrade.ui.trade;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jme.lsgoldtrade.databinding.ActivityHistoryEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderHisPageVo;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.HISTORYENTRUST)
public class HistoryEntrustActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityHistoryEntrustBinding mBinding;

    private EntrustAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mPagingKey = "";

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityHistoryEntrustBinding) mBindingUtil;

        initToolbar(R.string.trade_history_entrust, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(this, R.layout.item_entrust, null, "History");

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initOrderHisPage(true);
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

            initOrderHisPage(true);
        } else {
            showShortToast(R.string.trade_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;

            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));

            initOrderHisPage(true);
        } else {
            showShortToast(R.string.trade_end_time_error);
        }
    }

    private void initOrderHisPage(boolean enable) {
        mAdapter.clearDate();

        mCurrentPage = 1;
        mPagingKey = "";

        orderhispage(enable);
    }

    private void setEmptyData() {
        mBinding.swipeRefreshLayout.finishRefresh(false);
        mAdapter.loadMoreFail();
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void orderhispage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("beginDate", mBinding.tvStartTime.getText().toString());
        params.put("endDate", mBinding.tvEndTime.getText().toString());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().orderhispage, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "OrderHisPage":
                if (head.isSuccess()) {
                    OrderHisPageVo orderHisPageVo;

                    try {
                        orderHisPageVo = (OrderHisPageVo) response;
                    } catch (Exception e) {
                        orderHisPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == orderHisPageVo) {
                        setEmptyData();
                    } else {
                        bHasNext = orderHisPageVo.isHasNext();
                        mPagingKey = orderHisPageVo.getPagingKey();
                        List<OrderPageVo.OrderBean> orderBeanList = orderHisPageVo.getList();

                        if (bHasNext) {
                            if (mCurrentPage == 1)
                                mAdapter.setNewData(orderBeanList);
                            else
                                mAdapter.addData(orderBeanList);

                            mAdapter.loadMoreComplete();
                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == orderBeanList || 0 == orderBeanList.size()) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mAdapter.setNewData(orderBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mAdapter.addData(orderBeanList);
                                mAdapter.loadMoreComplete();
                            }

                            mBinding.swipeRefreshLayout.finishRefresh(true);
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
            if (bHasNext) {
                mCurrentPage++;

                orderhispage(true);
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initOrderHisPage(false);
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(HistoryEntrustActivity.this, (datePicker, year, month, dayOfMonth) -> {
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
