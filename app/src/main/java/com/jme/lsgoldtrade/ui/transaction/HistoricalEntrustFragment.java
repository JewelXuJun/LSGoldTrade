package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.jme.lsgoldtrade.databinding.FragmentHistoricalEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderHisPageVo;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.CancelOrderPopUpWindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistoricalEntrustFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    public FragmentHistoricalEntrustBinding mBinding;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mDate = "";
    private String mPagingKey = "";

    private List<Boolean> mList;

    private EntrustAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private View mEmptyView;
    private CancelOrderPopUpWindow mCancelOrderPopUpWindow;

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_historical_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mCancelOrderPopUpWindow = new CancelOrderPopUpWindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(mContext, R.layout.item_entrust, null, "History");
        mList = new ArrayList<>();

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
        initOrderHisPage(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.btn_cancel_order:
                    OrderPageVo.OrderBean orderBean = (OrderPageVo.OrderBean) adapter.getItem(position);

                    if (null == orderBean)
                        return;

                    if (null != mCancelOrderPopUpWindow && !mCancelOrderPopUpWindow.isShowing()) {
                        String time = orderBean.getDeclareTime();
                        String contractId = orderBean.getContractId();

                        mCancelOrderPopUpWindow.setData(contractId, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"),
                                MarketUtil.getTradeDirection(orderBean.getBsFlag()) + MarketUtil.getOCState(orderBean.getOcFlag()),
                                orderBean.getMatchPriceStr(), String.valueOf(orderBean.getEntrustNumber()),
                                String.valueOf(orderBean.getRemnantNumber()), MarketUtil.getEntrustState(orderBean.getStatus()),
                                (View) -> mCancelOrderPopUpWindow.dismiss(),
                                (View) -> {
                                    revocateorder(contractId, String.valueOf(orderBean.getOrderNo()));

                                    mCancelOrderPopUpWindow.dismiss();
                                });
                        mCancelOrderPopUpWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
                    }

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentHistoricalEntrustBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
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
            showShortToast(R.string.transaction_start_time_error);
        }
    }

    private void setEndTime(int year, int month, int dayOfMonth) {
        long endTime = getEndTime(year, month, dayOfMonth);

        if (endTime >= mStartTime) {
            mEndTime = endTime;

            mBinding.tvEndTime.setText(DateUtil.dateToString(mEndTime));

            initOrderHisPage(true);
        } else {
            showShortToast(R.string.transaction_end_time_error);
        }
    }

    private void initOrderHisPage(boolean enable) {
        mCurrentPage = 1;
        mDate = "";
        mPagingKey = "";

        orderhispage(enable);
    }

    private void setListData(List<OrderPageVo.OrderBean> orderBeanList) {
        if (null == orderBeanList || 0 == orderBeanList.size())
            return;

        for (int i = 0; i < orderBeanList.size(); i++) {
            OrderPageVo.OrderBean orderBean = orderBeanList.get(i);

            if (null != orderBean) {
                String date = orderBean.getTradeDate();

                mList.add(!mDate.equals(date));

                mDate = date;
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

    private void orderhispage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("beginDate", mBinding.tvStartTime.getText().toString());
        params.put("endDate", mBinding.tvEndTime.getText().toString());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().orderhispage, params, enable);
    }

    private void revocateorder(String contractId, String orderNo) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("orderNo", orderNo);

        sendRequest(TradeService.getInstance().revocateorder, params, true);
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
                            if (mCurrentPage == 1) {
                                mList.clear();
                                setListData(orderBeanList);

                                mAdapter.setNewData(orderBeanList);
                            } else {
                                setListData(orderBeanList);

                                mAdapter.addData(orderBeanList);
                            }

                            mAdapter.loadMoreComplete();
                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == orderBeanList || 0 == orderBeanList.size()) {
                                    mList.clear();
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mList.clear();
                                    setListData(orderBeanList);

                                    mAdapter.setNewData(orderBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                setListData(orderBeanList);

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
            case "RevocateOrder":
                if (head.isSuccess())
                    showShortToast(R.string.trade_cancel_order_success);

                initOrderHisPage(true);

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                orderhispage(false);
            } else {
                mAdapter.loadMoreEnd(true);
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
