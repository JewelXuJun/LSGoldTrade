package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentOwnConditionSheetBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ConditionPageVo;
import com.jme.lsgoldtrade.domain.ConditionSheetResponse;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.ConditionService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.SheetModifyPopUpWindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

public class OwnConditionSheetFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentOwnConditionSheetBinding mBinding;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mCurrentPage = 1;
    private int mTotalPages = 0;
    private boolean bAccountVoFlag = false;
    private boolean bPositionVoFlag = false;
    private String mSetDate = "";

    private List<Boolean> mList;
    private List<FiveSpeedVo> mFiveSpeedVoList;

    private ConditionOrderInfoVo mConditionOrderInfoVo;
    private AccountVo mAccountVo;
    private PositionVo mPositionVo;
    private ConditionSheetAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;
    private SheetModifyPopUpWindow mSheetModifyPopUpWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private View mEmptyView;
    private Subscription mRxbus;

    private static final int TIME_START = 0;
    private static final int TIME_END = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_MARKET_UPDATE:
                    mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);

                    getFiveSpeedQuotes();

                    break;
            }

            super.handleMessage(msg);
        }
    };

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
        mSheetModifyPopUpWindow = new SheetModifyPopUpWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initDate();
        initConditionOrderPage(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mConditionOrderInfoVo = (ConditionOrderInfoVo) adapter.getItem(position);

            if (null == mConditionOrderInfoVo)
                return;

            switch (view.getId()) {
                case R.id.btn_modify:
                    bAccountVoFlag = false;
                    bPositionVoFlag = false;

                    getAccount();
                    getPosition();

                    break;
                case R.id.btn_cancel:
                    if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                        mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_cancel_condition_sheet_message),
                                mContext.getResources().getString(R.string.text_confirm),
                                (v) -> {
                                    mConfirmPopupwindow.dismiss();

                                    revokeConditionOrder(String.valueOf(mConditionOrderInfoVo.getId()));
                                });
                        mConfirmPopupwindow.showAtLocation(mBinding.tvStartTime, Gravity.CENTER, 0, 0);
                    }

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

    @Override
    public void onResume() {
        super.onResume();

        getFiveSpeedQuotes();
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_MARKET_UPDATE);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_SHEET_MODIFY:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    List<String> list = (List<String>) object;

                    if (null == list || 4 != list.size())
                        return;

                    updateConditionOrder(list.get(0), list.get(1), list.get(2), list.get(3));

                    break;
            }
        });
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

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void showModifyPopUpWindow() {
        if (bAccountVoFlag && bPositionVoFlag
                && null != mSheetModifyPopUpWindow && !mSheetModifyPopUpWindow.isShowing()) {
            FiveSpeedVo fiveSpeedVoValue = null;

            for (FiveSpeedVo fiveSpeedVo : mFiveSpeedVoList) {
                if (null != fiveSpeedVo && fiveSpeedVo.getContractId().equals(mConditionOrderInfoVo.getContractId()))
                    fiveSpeedVoValue = fiveSpeedVo;
            }

            mSheetModifyPopUpWindow.setData(fiveSpeedVoValue, mAccountVo, mPositionVo,
                    mContract.getContractInfoFromID(mConditionOrderInfoVo.getContractId()), mConditionOrderInfoVo);
            mSheetModifyPopUpWindow.showAtLocation(mBinding.tvStartTime, Gravity.BOTTOM, 0, 0);
        }
    }

    private void queryConditionOrderPage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("current", String.valueOf(mCurrentPage));
        params.put("pageSize", "10");
        params.put("beginDate", mBinding.tvStartTime.getText().toString());
        params.put("endDate", mBinding.tvEndTime.getText().toString());
        params.put("type", "1");

        DTRequest request = new DTRequest(ConditionService.getInstance().queryConditionOrderPage, params, enable, false);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        ConditionSheetResponse dtResponse = (ConditionSheetResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
    }

    private void revokeConditionOrder(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);

        sendRequest(ConditionService.getInstance().revokeConditionOrder, params, true);
    }

    private void updateConditionOrder(String id, String effectiveTimeFlag, String triggerPrice, String entrustNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("effectiveTimeFlag", effectiveTimeFlag);
        params.put("triggerPrice", String.valueOf(new BigDecimal(triggerPrice).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", entrustNumber);

        sendRequest(ConditionService.getInstance().updateConditionOrder, params, true);
    }

    private void getFiveSpeedQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false, false, false);
    }

    private void getAccount() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, false);
    }

    private void getPosition() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("pagingKey", "");

        sendRequest(TradeService.getInstance().position, params, true);
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
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_cancel_success);

                    initConditionOrderPage(true);
                }

                break;
            case "UpdateConditionOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_modify_success);

                    initConditionOrderPage(true);
                }

                break;
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    try {
                        mFiveSpeedVoList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        mFiveSpeedVoList = null;

                        e.printStackTrace();
                    }

                    if (null != mSheetModifyPopUpWindow)
                        mSheetModifyPopUpWindow.setFiveSpeedVo(mFiveSpeedVoList);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_MARKET_UPDATE, getTimeInterval());
                }

                break;
            case "Account":
                if (head.isSuccess()) {
                    try {
                        mAccountVo = (AccountVo) response;
                    } catch (Exception e) {
                        mAccountVo = null;

                        e.printStackTrace();
                    }

                    bAccountVoFlag = true;

                    showModifyPopUpWindow();
                }

                break;
            case "Position":
                if (head.isSuccess()) {
                    PositionPageVo positionPageVo;

                    try {
                        positionPageVo = (PositionPageVo) response;
                    } catch (Exception e) {
                        positionPageVo = null;

                        e.printStackTrace();
                    }

                    if (null != positionPageVo) {
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo && positionVo.getContractId().equals(mConditionOrderInfoVo.getContractId())) {
                                    if (mConditionOrderInfoVo.getBsFlag() == 1 && positionVo.getType().equals("多"))
                                        mPositionVo = positionVo;
                                    else if (mConditionOrderInfoVo.getBsFlag() == 2 && positionVo.getType().equals("空"))
                                        mPositionVo = positionVo;
                                }
                            }
                        }

                        if (positionPageVo.isHasNext()) {
                            getPosition();
                        } else {
                            bPositionVoFlag = true;

                            showModifyPopUpWindow();
                        }
                    } else {
                        bPositionVoFlag = true;

                        showModifyPopUpWindow();
                    }
                }

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
