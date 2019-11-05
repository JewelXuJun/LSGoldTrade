package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.FragmentOwnConditionSheetBinding;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ConditionPageVo;
import com.jme.lsgoldtrade.service.ConditionService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OwnConditionSheetFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentOwnConditionSheetBinding mBinding;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private int mTotalPages = 0;
    private String mSetDate = "";

    private List<Boolean> mList;

    private ConditionSheetAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_own_condition_sheet;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mList = new ArrayList<>();
        mAdapter = new ConditionSheetAdapter(mContext, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
        initConditionOrderPage(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.btn_modify:

                    break;
                case R.id.btn_cancel:

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentOwnConditionSheetBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initDate() {
        getTodayCalendar();

        mStartTime = getStartTime(mYear, mMonth, mDayOfMonth) - 30 * AppConfig.DAY;
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

            initConditionOrderPage(true);
        } else {
            showShortToast(R.string.transaction_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;

            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));

            initConditionOrderPage(true);
        } else {
            showShortToast(R.string.transaction_end_time_error);
        }
    }

    private void initConditionOrderPage(boolean enable) {
        mCurrentPage = 1;
        mSetDate = "";
        mList.clear();

        queryConditionOrderPage(enable);
    }

    private void setListData(List<ConditionOrderInfoVo> conditionOrderInfoVos) {
        if (null == conditionOrderInfoVos || 0 == conditionOrderInfoVos.size())
            return;

        for (ConditionOrderInfoVo conditionOrderInfoVo : conditionOrderInfoVos) {
            if (null != conditionOrderInfoVo) {
                String setDate = conditionOrderInfoVo.getSetDate();

                mList.add(!mSetDate.equals(setDate));

                mSetDate = setDate;
            }
        }

        mAdapter.setList(mList);
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

    private void queryConditionOrderPage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("current", String.valueOf(mCurrentPage));
        params.put("pageSize", "10");
        params.put("beginDate", mBinding.tvStartTime.getText().toString());
        params.put("endDate", mBinding.tvEndTime.getText().toString());
        params.put("type", "1");

        sendRequest(ConditionService.getInstance().queryConditionOrderPage, params, enable);
    }

    private void revokeConditionOrder(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);

        sendRequest(ConditionService.getInstance().revokeConditionOrder, params, true);
    }

    private void updateConditionOrder(String effectiveTimeFlag, String entrustNumber, String id,
                                      String stopLossPrice, String stopProfitPrice, String triggerPrice) {
        HashMap<String, String> params = new HashMap<>();
        params.put("effectiveTimeFlag", effectiveTimeFlag);
        params.put("entrustNumber", entrustNumber);
        params.put("id", id);
        params.put("stopLossPrice", stopLossPrice);
        params.put("stopProfitPrice", stopProfitPrice);
        params.put("triggerPrice", triggerPrice);

        sendRequest(ConditionService.getInstance().updateConditionOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QueryConditionOrderPage":
                if (head.isSuccess()) {
                    ConditionPageVo conditionPageVo;

                    try {
                        conditionPageVo = (ConditionPageVo) response;
                    } catch (Exception e) {
                        conditionPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == conditionPageVo) {
                        setEmptyData();
                    } else {
                        mTotalPages = conditionPageVo.getPages();
                        List<ConditionPageVo.conditionOrderInfoBean> conditionOrderInfoBeanList = conditionPageVo.getRecords();
                        List<ConditionOrderInfoVo> conditionOrderInfoVoList = new ArrayList<>();

                        if (null != conditionOrderInfoBeanList && 0 != conditionOrderInfoBeanList.size()) {
                            for (ConditionPageVo.conditionOrderInfoBean conditionOrderInfoBean : conditionOrderInfoBeanList) {
                                if (null != conditionOrderInfoBean) {
                                    List<ConditionOrderInfoVo> list = conditionOrderInfoBean.getConditionOrderInfoList();

                                    if (null != list && 0 != list.size()) {
                                        for (ConditionOrderInfoVo infoVo : list) {
                                            if (null != infoVo)
                                                conditionOrderInfoVoList.add(infoVo);
                                        }
                                    }
                                }
                            }
                        }

                        setListData(conditionOrderInfoVoList);

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(conditionOrderInfoVoList);

                            if (null == conditionOrderInfoVoList || 0 == conditionOrderInfoVoList.size())
                                mAdapter.setEmptyView(getEmptyView());
                        } else {
                            mAdapter.addData(conditionOrderInfoVoList);
                            mAdapter.loadMoreComplete();
                        }

                        mBinding.swipeRefreshLayout.finishRefresh(true);
                    }
                } else {
                    setEmptyData();
                }

                break;
            case "RevokeConditionOrder":

                break;
            case "UpdateConditionOrder":

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (mCurrentPage < mTotalPages) {
                mCurrentPage++;

                queryConditionOrderPage(false);
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initConditionOrderPage(true);
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(mContext, (datePicker, year, month, dayOfMonth) -> {
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
