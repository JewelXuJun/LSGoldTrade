package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyDetailBinding;
import com.jme.lsgoldtrade.domain.TransactionDetailVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ElectronicCardDetailFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    private FragmentElectronicCardMoneyDetailBinding mBinding;

    private ElectronicCardAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;

    private String bHasNext = "";
    private boolean bVisibleToUser = false;
    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_electronic_card_money_detail;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new ElectronicCardAdapter(mContext, R.layout.item_electronic_card, null);

        mBinding.recyclerView.setHasFixedSize(false);
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
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentElectronicCardMoneyDetailBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHanlders());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (bVisibleToUser && null != mBinding)
            initQueryTransactionDetail(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            initQueryTransactionDetail(true);
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

            initQueryTransactionDetail(true);
        } else {
            showShortToast(R.string.transaction_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;
            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));

            initQueryTransactionDetail(true);
        } else {
            showShortToast(R.string.transaction_end_time_error);
        }
    }

    private void initQueryTransactionDetail(boolean enablle) {
        mCurrentPage = 1;

        queryTransactionDetail(enablle, "1");
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

    private void queryTransactionDetail(boolean enable, String queryMode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("queryMode", queryMode);
        params.put("Page", String.valueOf(mCurrentPage));
        params.put("beginDate", DateUtil.dateToAllString(mStartTime));
        params.put("endDate", DateUtil.dateToAllString(mEndTime));

        sendRequest(TradeService.getInstance().queryTransactionDetail, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QueryTransactionDetail":
                if (head.isSuccess()) {
                    TransactionDetailVo transactionDetailVo;

                    try {
                        transactionDetailVo = (TransactionDetailVo) response;
                    } catch (Exception e) {
                        transactionDetailVo = null;

                        e.printStackTrace();
                    }

                    if (null == transactionDetailVo) {
                        setEmptyData();
                    } else {
                        bHasNext = transactionDetailVo.getHasNext();
                        List<TransactionDetailVo.RecordsBean> recordsBeanList = transactionDetailVo.getRecords();

                        if (bHasNext.equals("1")) {
                            if (mCurrentPage == 1)
                                mAdapter.setNewData(recordsBeanList);
                            else
                                mAdapter.addData(recordsBeanList);

                            mAdapter.loadMoreComplete();
                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == recordsBeanList || 0 == recordsBeanList.size()) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mAdapter.setNewData(recordsBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mAdapter.addData(recordsBeanList);
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
            if (bHasNext.equals("1")) {
                mCurrentPage++;

                queryTransactionDetail(true, "3");
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initQueryTransactionDetail(false);
    }

    public class ClickHanlders {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(mActivity, (datePicker, year, month, dayOfMonth) -> {
                if (timeType == TIME_START)
                    setStartTime(year, month, dayOfMonth);
                else if (timeType == TIME_END)
                    setEndTime(year, month, dayOfMonth);
            }, mYear, mMonth, mDayOfMonth);

            mDatePickerDialog.show();
        }

    }
}
