package com.jme.lsgoldtrade.ui.personal;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.datai.common.charts.common.Config;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.domain.AccountDetailVo;
import com.jme.lsgoldtrade.domain.UserDetailsVo;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityDetailsBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 账户明细
 */
@Route(path = Constants.ARouterUriConst.DETAILS)
public class DetailsActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityDetailsBinding mBinding;

    private DetailsAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private int mTotalPages = 0;

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_account_detail, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new DetailsAdapter(null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
        initAccountDetail(true);
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

        mBinding = (ActivityDetailsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initDate() {
        getTodayCalendar();

        mStartTime = getStartTime(mYear, mMonth, mDayOfMonth) - 2 * Config.DAY;
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

            initAccountDetail(true);
        } else {
            showShortToast(R.string.transaction_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;

            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));

            initAccountDetail(true);
        } else {
            showShortToast(R.string.transaction_end_time_error);
        }
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

    private void initAccountDetail(boolean enable) {
        mCurrentPage = 1;

        getAccountDetailList(enable);
    }

    private void getAccountDetailList(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("beginDate", mBinding.tvStartTime.getText().toString());
        params.put("endDate", mBinding.tvEndTime.getText().toString());
        params.put("current", String.valueOf(mCurrentPage));
        params.put("pageSize", "10");

        sendRequest(ManagementService.getInstance().accountDetailList, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "AccountDetailList":
                if (head.isSuccess()) {
                    AccountDetailVo accountDetailVo;

                    try {
                        accountDetailVo = (AccountDetailVo) response;
                    } catch (Exception e) {
                        accountDetailVo = null;

                        e.printStackTrace();
                    }

                    if (null == accountDetailVo) {
                        setEmptyData();
                    } else {
                        List<UserDetailsVo> orderBeanList = accountDetailVo.getRecords();

                        mTotalPages = accountDetailVo.getPages();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(orderBeanList);

                            if (null == orderBeanList || 0 == orderBeanList.size())
                                mAdapter.setEmptyView(getEmptyView());
                        } else {
                            mAdapter.addData(orderBeanList);
                            mAdapter.loadMoreComplete();
                        }

                        mBinding.swipeRefreshLayout.finishRefresh(true);
                    }
                } else {
                    setEmptyData();
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initAccountDetail(false);
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (mCurrentPage < mTotalPages) {
                mCurrentPage++;

                getAccountDetailList(false);
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }, 0);
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(DetailsActivity.this, (datePicker, year, month, dayOfMonth) -> {
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
